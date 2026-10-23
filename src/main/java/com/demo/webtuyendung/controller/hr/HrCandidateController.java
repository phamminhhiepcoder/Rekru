package com.demo.webtuyendung.controller.hr;


import com.demo.webtuyendung.entity.CandidateEntity;
import com.demo.webtuyendung.entity.PostEntity;
import com.demo.webtuyendung.entity.UserEntity;
import com.demo.webtuyendung.enums.CVEvaluationStatus;
import com.demo.webtuyendung.enums.CandidateStatus;
import com.demo.webtuyendung.enums.Round;
import com.demo.webtuyendung.mapper.CandidateMapper;
import com.demo.webtuyendung.payload.CandidateDto;
import com.demo.webtuyendung.repository.*;
import com.demo.webtuyendung.service.GitHubUploadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/hr/post/candidate")
public class HrCandidateController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PostRepository postRepository;


    @Autowired
    private GitHubUploadService pdfService;

    private Integer postId = 0;

    @GetMapping("/add/{id}")
    public String addForm(Model model, @PathVariable Integer id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);

        if (userEntity == null) {
            return "redirect:/login";
        }
        this.postId = id;
        CandidateDto candidateDto = new CandidateDto();
        model.addAttribute("dto", candidateDto);
        return "hr/candidate/add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("dto") @Valid CandidateDto candidateDto,
                       @RequestParam(required = false) MultipartFile file,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            return "hr/candidate/add";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);

        if (userEntity == null) {
            return "redirect:/login";
        }
        CandidateEntity candidate = CandidateMapper.toEntity(candidateDto);
        candidate.setCandidateStatus(CandidateStatus.IN_REVIEW);
        PostEntity post = postRepository.findById(this.postId).get();
        candidate.setPost(post);
        candidate.setUpdatedAt(LocalDateTime.now());
        candidate.setCVEvaluationStatus(CVEvaluationStatus.IN_PROGRESS);
        candidate.setStatus(0);
        candidate = candidateRepository.save(candidate);

        if (file != null && !file.isEmpty()) {
            String filePath = "cv/" + file.getOriginalFilename();
            String url = pdfService.uploadFile(file, filePath);
            candidate.setCvFile(url);
        }
        candidateRepository.save(candidate);

        return "redirect:/hr/post/detail/" + this.postId + "?add=true";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        CandidateEntity candidateEntity = candidateRepository.findById(id).orElse(null);

        if (candidateEntity != null) {
            CandidateDto dto = CandidateMapper.toDto(candidateEntity);
            model.addAttribute("dto", dto);
            return "hr/candidate/detail";
        }

        return "redirect:/hr/post/detail/" + this.postId;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("candidateIds") List<Integer> candidateIds, Model model) {
        if (candidateIds != null && !candidateIds.isEmpty()) {
            for (Integer id : candidateIds) {
                CandidateEntity candidateEntity = candidateRepository.findById(id).orElse(null);
                if (candidateEntity != null) {
                    candidateRepository.delete(candidateEntity);
                }
            }
        }
        return "redirect:/hr/post/detail/" + this.postId + "?delete=true";
    }

    @PostMapping("/updateStatus")
    @ResponseBody
    public ResponseEntity<String> updateCandidateStatus(@RequestParam("id") Integer id,
                                                        @RequestParam("candidateStatus") CandidateStatus candidateStatus) {
        CandidateEntity candidate = candidateRepository.findById(id).orElse(null);
        if (candidate != null) {
            candidate.setCandidateStatus(candidateStatus);
            candidateRepository.save(candidate);
            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy ứng viên");
    }

    @PostMapping("/updateRound")
    @ResponseBody
    public ResponseEntity<String> updateRound(@RequestParam("id") Integer id,
                                                           @RequestParam("round") Round round) {
        CandidateEntity candidate = candidateRepository.findById(id).orElse(null);
        if (candidate != null) {
            candidate.setRound(round);
            candidateRepository.save(candidate);
            return ResponseEntity.ok("Cập nhật đánh giá CV thành công");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy ứng viên");
    }

}
