package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.model.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    private final ModelMapper modelMapper;

    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public RoleDto toDto(Role entity) {
        return modelMapper.map(entity, RoleDto.class);
    }

    public Role fromDto(RoleDto dto) {
        return modelMapper.map(dto, Role.class);
    }

}
