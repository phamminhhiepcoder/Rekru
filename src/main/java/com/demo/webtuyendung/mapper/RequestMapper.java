package com.demo.webtuyendung.mapper;

import com.demo.webtuyendung.entity.EmployeeEntity;
import com.demo.webtuyendung.entity.RequestEntity;
import com.demo.webtuyendung.enums.RequestStatus;
import com.demo.webtuyendung.payload.RequestDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RequestMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static RequestEntity toEntity(RequestDto dto, EmployeeEntity requester, EmployeeEntity interviewer) {
        RequestEntity entity = new RequestEntity();
        entity.setDepartment(dto.getDepartment());
        entity.setQuantity(dto.getQuantity());
        entity.setPosition(dto.getPosition());
        entity.setJobDescription(dto.getJobDescription());
        entity.setTarget(dto.getTarget());
        entity.setLocation(dto.getLocation());
        entity.setExplanation(dto.getExplanation());
        entity.setStandard(dto.getStandard());
        if (dto.getStartDate() != null && !dto.getStartDate().isEmpty()) {
            entity.setStartDate(LocalDate.parse(dto.getStartDate(), DATE_FORMATTER));
        }

        if (dto.getEndDate() != null && !dto.getEndDate().isEmpty()) {
            entity.setEndDate(LocalDate.parse(dto.getEndDate(), DATE_FORMATTER));
        }
        entity.setConfirmation(dto.getConfirmation());
        entity.setMonths(dto.getMonths());
        entity.setRequestType(dto.getRequestType());
        entity.setReason(dto.getReason());

        if (dto.getRequestStatus() != null) {
            entity.setRequestStatus(RequestStatus.valueOf(dto.getRequestStatus().toUpperCase()));
        }

        entity.setRequester(requester);
        entity.setReviewer(interviewer);

        return entity;
    }

    public static RequestDto toDto(RequestEntity entity) {
        RequestDto dto = new RequestDto();
        dto.setId(entity.getId());
        dto.setDepartment(entity.getDepartment());
        dto.setQuantity(entity.getQuantity());
        dto.setPosition(entity.getPosition());
        dto.setJobDescription(entity.getJobDescription());
        dto.setTarget(entity.getTarget());
        dto.setLocation(entity.getLocation());
        dto.setExplanation(entity.getExplanation());
        dto.setStandard(entity.getStandard());
        dto.setStartDate(entity.getStartDate() != null ? entity.getStartDate().format(DATE_FORMATTER) : null);
        dto.setEndDate(entity.getEndDate() != null ? entity.getEndDate().format(DATE_FORMATTER) : null);
        dto.setConfirmation(entity.getConfirmation());
        dto.setMonths(entity.getMonths());
        dto.setReason(entity.getReason());
        dto.setRequestType(entity.getRequestType());

        if (entity.getRequestStatus() != null) {
            dto.setRequestStatus(entity.getRequestStatus().name());
        }

        if (entity.getRequester() != null) {
            dto.setRequesterId(entity.getRequester().getId());
            dto.setRequesterName(entity.getRequester().getFullName());
        }

        if (entity.getReviewer() != null) {
            dto.setInterviewerId(entity.getReviewer().getId());
            dto.setInterviewerName(entity.getReviewer().getFullName());
        }

        return dto;
    }
}
