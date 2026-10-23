package com.demo.webtuyendung.controller.hr;


import com.demo.webtuyendung.entity.*;
import com.demo.webtuyendung.enums.RequestStatus;
import com.demo.webtuyendung.mapper.RequestMapper;
import com.demo.webtuyendung.payload.RequestDto;
import com.demo.webtuyendung.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hr/request")
public class HrRequestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public String getAll(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);

        if (userEntity == null) {
            return "redirect:/login";
        }

        List<RequestDto> requests = requestRepository.findAll().stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("requests", requests);
        return "hr/request/view";
    }


    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        RequestEntity requestEntity = requestRepository.findById(id).orElse(null);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);

        if (userEntity == null) {
            return "redirect:/login";
        }

        if (requestEntity != null) {
            List<PostEntity> posts = postRepository.findAllByRequest(requestEntity);
            RequestDto dto = RequestMapper.toDto(requestEntity);
            model.addAttribute("requestDto", dto);
            model.addAttribute("posts", posts);
            return "hr/request/detail";
        }

        return "redirect:/hr/request";
    }
}
