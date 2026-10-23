package com.demo.webtuyendung.controller.hr;

import com.demo.webtuyendung.dto.CVAnalysisResult;
import com.demo.webtuyendung.entity.CandidateEntity;
import com.demo.webtuyendung.repository.CandidateRepository;
import com.demo.webtuyendung.service.FileProcessingService;
import com.demo.webtuyendung.service.GrokAIService;
import com.demo.webtuyendung.service.RemoteFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/hr/post/candidate")
@RequiredArgsConstructor
public class HrPostActionsController {

  private final CandidateRepository candidateRepo;
  private final RemoteFileService remoteFileService;
  private final FileProcessingService fileProcessingService;
  private final GrokAIService grokAIService;

  // Sàng lọc tất cả ứng viên thuộc một bài đăng, rồi quay lại trang chi tiết bài đăng
  @GetMapping("/screen/{postId}")
  public String screenByPost(@PathVariable Integer postId,
                             @RequestParam(defaultValue = "Java, Spring Boot, REST API, 2+ năm, tiếng Anh giao tiếp") String criteria,
                             RedirectAttributes ra) {
    // ⚠️ TUỲ DỰ ÁN: đổi method repo cho đúng
    // Ví dụ khả dĩ: findAllByPostId / findByPostEntity_Id / findAllByPost_Id ...
    List<CandidateEntity> list = candidateRepo.findAllByPostId(postId);

    int ok = 0, fail = 0;
    for (CandidateEntity c : list) {
      try {
        if (c.getCvFile() == null) { fail++; continue; }
        byte[] bytes = remoteFileService.download(c.getCvFile());
        String text = fileProcessingService.extractText(bytes, c.getCvFile());
        CVAnalysisResult result = grokAIService.analyze(criteria, text);

        // TODO: Lưu điểm vào DB nếu có cột (ví dụ):
        // c.setAiScore(result.getScore());
        // c.setAiSummary(result.getSummary());
        // candidateRepo.save(c);

        ok++;
      } catch (Exception e) {
        fail++;
      }
    }

    ra.addFlashAttribute("message",
        String.format("Đã sàng lọc %d ứng viên (thất bại %d).", ok, fail));

    // quay lại trang chi tiết bài đăng
    return "redirect:/hr/post/detail/" + postId;
  }
}
