package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.model.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleService {

    private final ModelMapper modelMapper;

    public RoleService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RoleDto toDto(Role entity) {
        return modelMapper.map(entity, RoleDto.class);
    }

    public Role fromDto(RoleDto dto) {
        return modelMapper.map(dto, Role.class);
    }

}
