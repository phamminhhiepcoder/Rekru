package com.demo.webtuyendung.controller.dep;


import com.demo.webtuyendung.entity.*;
import com.demo.webtuyendung.enums.ContractType;
import com.demo.webtuyendung.enums.RequestStatus;
import com.demo.webtuyendung.mapper.CandidateMapper;
import com.demo.webtuyendung.mapper.PostMapper;
import com.demo.webtuyendung.mapper.RequestMapper;
import com.demo.webtuyendung.payload.CandidateDto;
import com.demo.webtuyendung.payload.CommentRequestDto;
import com.demo.webtuyendung.payload.PostDto;
import com.demo.webtuyendung.payload.RequestDto;
import com.demo.webtuyendung.repository.*;
import com.demo.webtuyendung.service.GitHubUploadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dep/request")
public class DepRequestController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private GitHubUploadService uploadService;


    @GetMapping
    public String getAll(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);

        if (userEntity == null) {
            return "redirect:/login";
        }

        Optional<EmployeeEntity> employeeOpt = employeeRepository.findByUser(userEntity);
        if (employeeOpt.isEmpty()) {
            return "redirect:/login";
        }

        EmployeeEntity requester = employeeOpt.get();

        List<RequestDto> requests = requestRepository.findAll().stream()
                .filter(req -> req.getDepartment() != null && req.getDepartment().equals(requester.getDepartment()))
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("requests", requests);
        return "dep/request/view";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("requestDto", new RequestDto());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);

        if (userEntity == null) {
            return "redirect:/login";
        }

        Optional<EmployeeEntity> employeeOpt = employeeRepository.findByUser(userEntity);
        if (employeeOpt.isEmpty()) {
            return "redirect:/login";
        }
        EmployeeEntity requester = employeeOpt.get();
        model.addAttribute("department", requester.getDepartment());
        model.addAttribute("requesterName", requester.getFullName());
        return "dep/request/add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("requestDto") @Valid RequestDto requestDto,
                       BindingResult result,
                       @RequestParam(required = false) MultipartFile file,
                       Model model) {
        if (result.hasErrors()) {
            return "dep/request/add";
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);

        if (userEntity == null) {
            return "redirect:/login";
        }

        Optional<EmployeeEntity> employeeOpt = employeeRepository.findByUser(userEntity);
        if (employeeOpt.isEmpty()) {
            return "redirect:/login";
        }

        EmployeeEntity requester = employeeOpt.get();
        requestDto.setDepartment(requester.getDepartment());

        RequestEntity requestEntity = RequestMapper.toEntity(requestDto, requester, null);

        if (file != null && !file.isEmpty()) {

            String filePath = "pdf/" + file.getOriginalFilename();
            String url = uploadService.uploadFile(file, filePath);
            System.out.println(url);
            requestEntity.setConfirmation(url);
        }

        if (requestEntity.getRequestStatus() == null) {
            requestEntity.setRequestStatus(RequestStatus.WAITING);
        }
        requestEntity.setContractType(ContractType.LONG_TERM);

        requestRepository.save(requestEntity);

        return "redirect:/dep/request?add=true";
    }


    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        RequestEntity requestEntity = requestRepository.findById(id).orElse(null);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);

        if (userEntity == null) {
            return "redirect:/login";
        }

        Optional<EmployeeEntity> employeeOpt = employeeRepository.findByUser(userEntity);
        if (employeeOpt.isEmpty()) {
            return "redirect:/login";
        }
        EmployeeEntity requester = employeeOpt.get();

        if (requestEntity != null) {
            RequestDto dto = RequestMapper.toDto(requestEntity);
            model.addAttribute("requestDto", dto);
            model.addAttribute("department", requester.getDepartment());
            model.addAttribute("requesterName", requester.getFullName());

            List<PostEntity> posts = postRepository.findAllByRequest(requestEntity);
            List<CandidateEntity> candidates = new ArrayList<>();
            List<CandidateDto> candidateDtos = new ArrayList<>();

            for (PostEntity post : posts) {
                candidates.addAll(candidateRepository.findAllByPostId(post.getId()));
            }

            for (CandidateEntity c : candidates) {
                CandidateDto candidateDto = CandidateMapper.toDto(c);
                if (candidateDto.getStatus() == 1) {
                    candidateDtos.add(candidateDto);
                }
            }

            model.addAttribute("candidates", candidateDtos);

            return "dep/request/detail";
        }

        return "redirect:/dep/request";
    }

    @PostMapping("/comment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> comment(@RequestBody CommentRequestDto commentRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<CandidateEntity> opt = candidateRepository.findById(commentRequest.getCandidateIdSendComment());
        if (opt.isEmpty()) {
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

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        RequestEntity requestEntity = requestRepository.findById(id).orElse(null);
        if (requestEntity != null) {
            RequestDto dto = RequestMapper.toDto(requestEntity);
            model.addAttribute("requestDto", dto);
            return "dep/request/edit";
        }

        return "redirect:/dep/request";
    }

    @PostMapping("/update")
    public String updateRequest(@ModelAttribute("requestDto") @Valid RequestDto requestDto,
                                BindingResult result,
                                @RequestParam(required = false) MultipartFile file,
                                Model model) {
        if (result.hasErrors()) {
            return "dep/request/edit";
        }

        RequestEntity requestEntity = requestRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid request ID"));

        EmployeeEntity requester = requestEntity.getRequester();

        EmployeeEntity interviewer = null;
        if (requestDto.getInterviewerId() != null) {
            interviewer = employeeRepository.findById(requestDto.getInterviewerId()).orElse(null);
        }
        requestDto.setDepartment(requester.getDepartment());
        requestDto.setRequestStatus(requestEntity.getRequestStatus().name());

        RequestEntity updatedEntity = RequestMapper.toEntity(requestDto, requester, interviewer);

        if (file != null && !file.isEmpty()) {
            String filePath = "pdf/" + file.getOriginalFilename();
            String url = uploadService.uploadFile(file, filePath);
            System.out.println(url);

            updatedEntity.setConfirmation(url);
        }
        updatedEntity.setId(requestEntity.getId());

        requestRepository.save(updatedEntity);

        return "redirect:/dep/request?update=true";
    }


}
