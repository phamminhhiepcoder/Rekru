package com.demo.webtuyendung.mapper;

import com.demo.webtuyendung.entity.EmployeeEntity;
import com.demo.webtuyendung.enums.Position;
import com.demo.webtuyendung.payload.EmployeeDto;

public class EmployeeMapper {
    public static EmployeeEntity toEntity(EmployeeDto dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setFullName(dto.getFullName());
        entity.setGender(dto.getGender());
        entity.setPosition(Position.valueOf(dto.getPosition().toUpperCase()));


        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public static EmployeeDto toDto(EmployeeEntity entity) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setGender(entity.getGender());
        dto.setPosition(entity.getPosition().getPositionName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setDepartment(entity.getDepartment());
        dto.setUserId(entity.getUser().getId());
        dto.setStatus(entity.getStatus());
        dto.setEmail(entity.getUser().getEmail());
        return dto;
    }
}
