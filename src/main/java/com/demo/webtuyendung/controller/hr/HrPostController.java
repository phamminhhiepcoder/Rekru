package com.demo.webtuyendung.controller.hr;


import com.demo.webtuyendung.entity.*;
import com.demo.webtuyendung.enums.InterviewType;
import com.demo.webtuyendung.enums.Round;
import com.demo.webtuyendung.mapper.CandidateMapper;
import com.demo.webtuyendung.mapper.PostMapper;
import com.demo.webtuyendung.payload.CandidateDto;
import com.demo.webtuyendung.payload.CommentRequestDto;
import com.demo.webtuyendung.payload.PostDto;
import com.demo.webtuyendung.repository.*;
import com.demo.webtuyendung.service.EmailSenderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@Controller
@RequestMapping("/hr/post")
public class HrPostController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    private Integer reqId = 0;
    private Integer postId = 0;

    @Autowired
    private CandidateRepository candidateRepository;

    @GetMapping("/add")
    public String addForm(Model model, @RequestParam Integer reqId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);

        if (userEntity == null) {
            return "redirect:/login";
        }
        this.reqId = reqId;
        RequestEntity request = requestRepository.findById(reqId).get();
        PostDto postDto = new PostDto();
        postDto.setPosition(request.getPosition());
        postDto.setDepartment(request.getDepartment());
        postDto.setLocation(request.getLocation());
        postDto.setContractType(request.getContractType().name());
        postDto.setMonths(request.getMonths());
        postDto.setJobDescription(request.getJobDescription());
        postDto.setStandard(request.getStandard());
        model.addAttribute("postDto", postDto);

        return "hr/post/add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("postDto") @Valid PostDto postDto,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            return "hr/post/add";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);

        if (userEntity == null) {
            return "redirect:/login";
        }
        RequestEntity request = requestRepository.findById(this.reqId).get();
        PostEntity post = PostMapper.toEntity(postDto, request);
        post.setStatus(1);
        postRepository.save(post);

        return "redirect:/hr/request/detail/" + this.reqId + "?add=true";
    }


    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id) {
        PostEntity postEntity = postRepository.findById(id).orElse(null);
        if (postEntity != null) {
            PostDto dto = PostMapper.toDto(postEntity);
            model.addAttribute("postDto", dto);
            return "hr/post/edit";
        }

        return "redirect:/hr/post";
    }

    @PostMapping("/update")
    public String updateRequest(@ModelAttribute("postDto") @Valid PostDto postDto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "hr/post/edit";
        }

        PostEntity postEntity = postRepository.findById(postDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid request ID"));

        RequestEntity request = postEntity.getRequest();

        PostEntity updatedEntity = PostMapper.toEntity(postDto, request);

        updatedEntity.setId(postEntity.getId());

        postRepository.save(updatedEntity);

        return "redirect:/hr/request/detail/" + request.getId() + "?update=true";
    }


    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        PostEntity postEntity = postRepository.findById(id).orElse(null);
        PostDto postDto = PostMapper.toDto(postEntity);
        model.addAttribute("postDto", postDto);
        List<CandidateEntity> candidates = candidateRepository.findAllByPostId(id);
        List<CandidateDto> candidateDtos = new ArrayList<>();

        for(CandidateEntity c : candidates) {
            CandidateDto candidateDto = CandidateMapper.toDto(c);
            candidateDtos.add(candidateDto);
        }

        this.postId = id;
        model.addAttribute("postDto", postDto);
        model.addAttribute("candidates", candidateDtos);
        return "hr/post/detail";
    }

    @PostMapping("/createSchedule")
    public String createSchedule(
            @RequestParam Integer candidateIdCreateSchedule,
            @RequestParam String scheduleRound,
            @RequestParam LocalDateTime scheduleDateTime,
            @RequestParam String scheduleType,
            @RequestParam String scheduleLocation,
            @RequestParam String scheduleEmail,
            @RequestParam String scheduleSendTime,
            @RequestParam String scheduleEmailTitle,
            @RequestParam String scheduleEmailContent
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<CandidateEntity> opt = candidateRepository.findById(candidateIdCreateSchedule);
        if(opt.isEmpty()) {
            return "redirect:/hr/post/detail/" + this.postId;
        }

        CandidateEntity candidate = opt.get();

        AppointmentEntity appointmentEntity = new AppointmentEntity();

        appointmentEntity.setCreatedAt(LocalDateTime.now());
        appointmentEntity.setCreatedBy(username);
        appointmentEntity.setUpdatedAt(LocalDateTime.now());
        appointmentEntity.setUpdatedBy(username);

        appointmentEntity.setAppointmentDate(scheduleDateTime);
        appointmentEntity.setCandidate(candidate);
        appointmentEntity.setInterviewType(InterviewType.valueOf(scheduleType));
        appointmentEntity.setLocation(scheduleLocation);
        appointmentEntity.setRound(Round.valueOf(scheduleRound));

        appointmentRepository.save(appointmentEntity);

        String[] emailArray = scheduleEmail.split("\\s*,\\s*");

        if (scheduleSendTime.equals("IMMEDIATELY")) {
            for (String s : emailArray) {
                String htmlContent = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px;'>"
                        + "<table style='width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>"
                        + "<tr>"
                        + "<td style='background-color: #007bff; color: white; text-align: center; padding: 20px 0;'>"
                        + "<h2 style='margin: 0;'>" + scheduleEmailContent + "</h2>"
                        + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td style='padding: 20px;'>"
                        + "<p><strong style='font-size: 18px;'>Xin Chào " + s + ",</strong></p>"
                        + "<p>Chúng tôi có thông báo quan trọng gửi đến bạn:</p>"
                        + "<p style='font-style: italic; color: #555;'>" + scheduleEmailContent + "</p>"
                        + "<br>"
                        + "<p><strong>Trân trọng,</strong></p>"
                        + "<p><strong>Bộ Phận HR Công Ty</strong></p>"
                        + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td style='background-color: #f1f1f1; text-align: center; padding: 10px;'>"
                        + "<p style='color: #888; font-size: 12px;'>Cảm ơn bạn đã quan tâm đến cơ hội tại công ty chúng tôi.</p>"
                        + "</td>"
                        + "</tr>"
                        + "</table>"
                        + "</body></html>";

                this.emailSenderService.sendHtmlEmail(s, scheduleEmailTitle, htmlContent);
            }
        }

        return "redirect:/hr/post/detail/" + this.postId + "?schedule=true";
    }

    @PostMapping("/sendCandidate")
    public String send(@RequestParam("candidateId") Integer candidateId, @RequestParam("notes") String notes, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<CandidateEntity> opt = candidateRepository.findById(candidateId);
        if(opt.isEmpty()) {
            return "redirect:/hr/post/detail/" + this.postId;
        }
        CandidateEntity candidate = opt.get();
        candidate.setNotes(notes);
        candidate.setStatus(1);
        candidate.setFullName(opt.get().getFullName());
        candidate.setUpdatedAt(LocalDateTime.now());
        candidate.setUpdatedBy(username);
        candidateRepository.save(candidate);
        return "redirect:/hr/post/detail/" + this.postId + "?send=true";
    }

    @PostMapping("/comment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> comment(@RequestBody CommentRequestDto commentRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<CandidateEntity> opt = candidateRepository.findById(commentRequest.getCandidateIdSendComment());
        if(opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Ứng viên không tìm thấy!"));
        }

        UserEntity userEntity = userRepository.findByEmail(username).get();
        Optional<EmployeeEntity> employeeOpt = employeeRepository.findByUser(userEntity);
        EmployeeEntity commenter = employeeOpt.get();

        CandidateEntity candidate = opt.get();
        CommentEntity comment = new CommentEntity();
        comment.setCandidate(candidate);
        comment.setReviewer(commenter);
        comment.setContent(commentRequest.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setCreatedBy(commenter.getFullName());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setUpdatedBy(commenter.getFullName());
        commentRepository.save(comment);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("reviewerName", commenter.getFullName());
        response.put("content", commentRequest.getContent());
        response.put("createdAt", comment.getCreatedAt().toString());

        return ResponseEntity.ok(response);
    }


}
