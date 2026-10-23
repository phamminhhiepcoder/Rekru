package com.demo.webtuyendung.mapper;

import com.demo.webtuyendung.entity.EmployeeEntity;
import com.demo.webtuyendung.entity.PostEntity;
import com.demo.webtuyendung.entity.RequestEntity;
import com.demo.webtuyendung.payload.PostDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static PostEntity toEntity(PostDto dto, RequestEntity request) {
        PostEntity entity = new PostEntity();

        entity.setWelfare(dto.getWelfare());
        entity.setInChargePerson(dto.getInChargePerson());
        entity.setShowPost(dto.getShowPost());
        entity.setStatus(dto.getStatus());
        entity.setWorkTime(dto.getWorkTime());
        entity.setPostSource(dto.getPostSource());

        if (dto.getStartDate() != null && !dto.getStartDate().isEmpty()) {
            entity.setStartDate(LocalDate.parse(dto.getStartDate(), FORMATTER));
        }

        if (dto.getEndDate() != null && !dto.getEndDate().isEmpty()) {
            entity.setEndDate(LocalDate.parse(dto.getEndDate(), FORMATTER));
        }

        entity.setRequest(request);
        entity.setCreator(null);
        return entity;
    }

    public static PostDto toDto(PostEntity entity) {
        PostDto dto = new PostDto();

        dto.setId(entity.getId());
        dto.setWelfare(entity.getWelfare());
        dto.setShowPost(entity.getShowPost());
        dto.setStatus(entity.getStatus());
        dto.setWorkTime(entity.getWorkTime());
        dto.setInChargePerson(entity.getInChargePerson());
        dto.setPostSource(entity.getPostSource());

        dto.setStartDate(entity.getStartDate() != null ? entity.getStartDate().format(FORMATTER) : null);
        dto.setEndDate(entity.getEndDate() != null ? entity.getEndDate().format(FORMATTER) : null);

        if (entity.getRequest() != null) {
            dto.setRequestId(entity.getRequest().getId());
            dto.setDepartment(entity.getRequest().getDepartment());
            dto.setPosition(entity.getRequest().getPosition());
            dto.setLocation(entity.getRequest().getLocation());
            dto.setJobDescription(entity.getRequest().getJobDescription());
            dto.setStandard(entity.getRequest().getStandard());
            dto.setMonths(entity.getRequest().getMonths());
            dto.setContractType(entity.getRequest().getContractType().name());
        }

        return dto;
    }
}
