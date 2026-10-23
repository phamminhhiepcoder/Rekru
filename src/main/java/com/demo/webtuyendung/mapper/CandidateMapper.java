package com.demo.webtuyendung.mapper;

import com.demo.webtuyendung.entity.CandidateEntity;
import com.demo.webtuyendung.entity.PostEntity;
import com.demo.webtuyendung.enums.CVEvaluationStatus;
import com.demo.webtuyendung.enums.CandidateStatus;
import com.demo.webtuyendung.enums.Round;
import com.demo.webtuyendung.payload.CandidateDto;

public class CandidateMapper {
    public static CandidateEntity toEntity(CandidateDto dto) {
        CandidateEntity entity = new CandidateEntity();

        entity.setFullName(dto.getFullName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmail(dto.getEmail());
        entity.setIntroducer(dto.getIntroducer());
        entity.setSource(dto.getSource());
        entity.setCvFile(dto.getCvFileUrl());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());

        if (dto.getRound() != null) {
            entity.setRound(Round.valueOf(dto.getRound()));
        }

        if (dto.getPostId() != null) {
            PostEntity post = new PostEntity();
            post.setId(dto.getPostId());
            entity.setPost(post);
        }

        if (dto.getcVEvaluationStatus() != null) {
            entity.setCVEvaluationStatus(CVEvaluationStatus.valueOf(dto.getcVEvaluationStatus()));
        }
        if (dto.getCandidateStatus() != null) {
            entity.setCandidateStatus(CandidateStatus.valueOf(dto.getCandidateStatus()));
        }

        return entity;
    }

    public static CandidateDto toDto(CandidateEntity entity) {
        CandidateDto dto = new CandidateDto();

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setSource(entity.getSource());
        dto.setCvFileUrl(entity.getCvFile());
        dto.setPosition(entity.getPost().getRequest().getPosition());
        dto.setCvFile(entity.getCvFile());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setRound(entity.getRound() != null ? entity.getRound().name() : null);

        dto.setIntroducer(entity.getIntroducer());

        dto.setcVEvaluationStatus(entity.getCVEvaluationStatus() != null ? entity.getCVEvaluationStatus().name() : null);
        dto.setCandidateStatus(entity.getCandidateStatus() != null ? entity.getCandidateStatus().name() : null);

        if (entity.getPost() != null) {
            dto.setPostId(entity.getPost().getId());
        }

        return dto;
    }
}
