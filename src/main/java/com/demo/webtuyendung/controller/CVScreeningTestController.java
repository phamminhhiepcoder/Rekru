package com.demo.webtuyendung.controller;

import com.demo.webtuyendung.dto.CVAnalysisResult;
import com.demo.webtuyendung.service.FileProcessingService;
import com.demo.webtuyendung.service.GrokAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/hr/cv-test")
@RequiredArgsConstructor
@Slf4j
public class CVScreeningTestController {

    private final GrokAIService grokAIService;
    private final FileProcessingService fileProcessingService;

    private final Map<String, byte[]> tempFileStorage = new HashMap<>();

    @GetMapping
    public String testPage() {
        return "hr/cv-screening-test";
    }

    @PostMapping("/api/upload")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Uploading file: {}", file.getOriginalFilename());

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "File trống"));
            }

            if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Chỉ chấp nhận file PDF"));
            }

            String fileId = UUID.randomUUID().toString();

            tempFileStorage.put(fileId, file.getBytes());

            log.info("File uploaded successfully with ID: {}", fileId);

            return ResponseEntity.ok(Map.of(
                    "fileId", fileId,
                    "fileName", file.getOriginalFilename(),
                    "size", file.getSize()
            ));

        } catch (Exception e) {
            log.error("Error uploading file", e);
            return ResponseEntity.status(500).body(
                    Map.of("error", "Lỗi upload: " + e.getMessage()));
        }
    }

    @PostMapping("/api/analyze")
    @ResponseBody
    public ResponseEntity<?> analyzeCV(@RequestBody Map<String, Object> request) {
        try {
            log.info("========== START ANALYSIS ==========");

            String criteria = (String) request.get("criteria");
            List<String> fileIds = (List<String>) request.get("fileIds");

            log.info("Criteria: {}", criteria);
            log.info("Number of files: {}", fileIds.size());

            if (criteria == null || criteria.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Thiếu tiêu chí tuyển dụng"));
            }

            if (fileIds == null || fileIds.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Chưa có file CV nào"));
            }

            List<CVAnalysisResult> results = new ArrayList<>();

            for (String fileId : fileIds) {
                log.info("Analyzing file: {}", fileId);

                byte[] fileBytes = tempFileStorage.get(fileId);
                if (fileBytes == null) {
                    log.error("File not found: {}", fileId);
                    continue;
                }

                try {
                    log.info("Step 1: Extracting text from PDF...");
                    String cvText = fileProcessingService.extractText(fileBytes, "file.pdf");
                    log.info("Extracted {} characters", cvText.length());

                    if (cvText.length() < 50) {
                        log.warn("CV text too short, might be extraction issue");
                    }

                    log.info("Step 2: Calling Grok AI...");
                    CVAnalysisResult result = grokAIService.analyze(criteria, cvText);

                    log.info("Analysis completed - Score: {}, Name: {}",
                            result.getScore(), result.getName());

                    results.add(result);

                } catch (Exception e) {
                    log.error("Error analyzing file: {}", fileId, e);

                    CVAnalysisResult errorResult = new CVAnalysisResult();
                    errorResult.setScore(0);
                    errorResult.setName("Lỗi phân tích");
                    errorResult.setEmail("N/A");
                    errorResult.setPhone("N/A");
                    errorResult.setSummary("Không thể phân tích CV: " + e.getMessage());
                    errorResult.setStrengths(Collections.emptyList());
                    errorResult.setWeaknesses(Collections.singletonList("Lỗi: " + e.getMessage()));

                    results.add(errorResult);
                }
            }

            log.info("========== END ANALYSIS ==========");
            log.info("Total results: {}", results.size());

            return ResponseEntity.ok(Map.of("results", results));

        } catch (Exception e) {
            log.error("========== ANALYSIS FAILED ==========", e);
            return ResponseEntity.status(500).body(
                    Map.of("error", "Lỗi phân tích: " + e.getMessage()));
        }
    }

    @DeleteMapping("/api/clear")
    @ResponseBody
    public ResponseEntity<?> clearTempFiles() {
        int count = tempFileStorage.size();
        tempFileStorage.clear();
        log.info("Cleared {} temporary files", count);
        return ResponseEntity.ok(Map.of("message", "Đã xóa " + count + " files tạm"));
    }
}