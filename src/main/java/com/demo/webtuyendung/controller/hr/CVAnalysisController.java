package com.demo.webtuyendung.controller.hr;

import com.demo.webtuyendung.dto.CVAnalysisResult;
import com.demo.webtuyendung.entity.CandidateEntity;
import com.demo.webtuyendung.repository.CandidateRepository;
import com.demo.webtuyendung.service.FileProcessingService;
import com.demo.webtuyendung.service.GrokAIService;
import com.demo.webtuyendung.service.RemoteFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/hr/cv")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class CVAnalysisController {

    private final CandidateRepository candidateRepo;
    private final RemoteFileService remoteFileService;
    private final FileProcessingService fileProcessingService;
    private final GrokAIService grokAIService;

    // ========== THAY THẾ METHOD NÀY ==========
    @PostMapping("/analyze/{candidateId}")
    public ResponseEntity<?> analyzeOne(
        @PathVariable Integer candidateId,
        @RequestParam(defaultValue = "") String criteria) {

        log.info("========== START ANALYSIS ==========");
        log.info("Candidate ID: {}", candidateId);
        log.info("Criteria: {}", criteria);

        // Nếu không có criteria, dùng mặc định
        if (criteria.isEmpty()) {
            criteria = "Java, Spring Boot, REST API, 2+ năm, tiếng Anh giao tiếp";
            log.info("Using default criteria: {}", criteria);
        }

        CandidateEntity cand = candidateRepo.findById(candidateId).orElse(null);
        if (cand == null) {
            log.error("Candidate not found: {}", candidateId);
            return ResponseEntity.status(404).body(
                Map.of("error", "Không tìm thấy ứng viên ID: " + candidateId));
        }

        if (cand.getCvFile() == null || cand.getCvFile().isEmpty()) {
            log.error("Candidate has no CV file: {}", candidateId);
            return ResponseEntity.status(404).body(
                Map.of("error", "Ứng viên chưa có CV"));
        }

        try {
            log.info("CV file URL: {}", cand.getCvFile());
            
            log.info("Step 1: Downloading CV...");
            byte[] bytes = remoteFileService.download(cand.getCvFile());
            log.info("Downloaded {} bytes", bytes.length);

            log.info("Step 2: Extracting text...");
            String text = fileProcessingService.extractText(bytes, cand.getCvFile());
            log.info("Extracted {} characters", text.length());
            log.debug("First 200 chars: {}", text.substring(0, Math.min(200, text.length())));

            log.info("Step 3: Calling Grok AI...");
            CVAnalysisResult result = grokAIService.analyze(criteria, text);
            log.info("Analysis completed - Score: {}", result.getScore());
            
            log.info("========== END ANALYSIS ==========");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("========== ANALYSIS FAILED ==========", e);
            return ResponseEntity.status(500).body(
                Map.of("error", "Lỗi: " + e.getMessage()));
        }
    }

}