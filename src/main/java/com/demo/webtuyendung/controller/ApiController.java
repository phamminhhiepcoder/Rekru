package com.demo.webtuyendung.controller;


import com.demo.webtuyendung.mapper.CandidateMapper;
import com.demo.webtuyendung.mapper.CommentMapper;
import com.demo.webtuyendung.mapper.EmployeeMapper;
import com.demo.webtuyendung.payload.CandidateDto;
import com.demo.webtuyendung.payload.CommentDto;
import com.demo.webtuyendung.payload.EmployeeDto;
import com.demo.webtuyendung.repository.CandidateRepository;
import com.demo.webtuyendung.repository.CommentRepository;
import com.demo.webtuyendung.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/candidate/{id}")
    public CandidateDto candidateDto(@PathVariable Integer id) {
        CandidateDto candidateDto = CandidateMapper.toDto(candidateRepository.findById(id).get());

        List<CommentDto> commentDtos = commentRepository.findAllByCandidateId(id).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());

        candidateDto.setCommentDtos(commentDtos);

        return candidateDto;
    }
}