package com.demo.webtuyendung.mapper;

import com.demo.webtuyendung.entity.*;
import com.demo.webtuyendung.enums.CVEvaluationStatus;
import com.demo.webtuyendung.enums.CandidateStatus;
import com.demo.webtuyendung.enums.Round;
import com.demo.webtuyendung.payload.CandidateDto;
import com.demo.webtuyendung.payload.CommentDto;
import com.demo.webtuyendung.repository.CandidateRepository;
import com.demo.webtuyendung.repository.EmployeeRepository;
import com.demo.webtuyendung.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CommentMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    public CommentEntity toEntity(CommentDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);
        Optional<EmployeeEntity> employeeOpt = employeeRepository.findByUser(userEntity);

        EmployeeEntity reviewer = employeeOpt.get();

        CommentEntity entity = new CommentEntity();

        CandidateEntity candidate = candidateRepository.findById(dto.getCandidateId()).get();
        entity.setCandidate(candidate);
        entity.setContent(dto.getContent());
        entity.setReviewer(reviewer);
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);

        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        return entity;
    }

    public static CommentDto toDto(CommentEntity entity) {
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setCandidateId(entity.getCandidate().getId());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().format(FORMATTER) : null);
        dto.setReviewerId(entity.getReviewer().getId());
        dto.setReviewerName(entity.getReviewer().getFullName());
        return dto;
    }
}
