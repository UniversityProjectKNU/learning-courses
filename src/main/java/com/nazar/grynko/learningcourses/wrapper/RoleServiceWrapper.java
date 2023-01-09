package com.nazar.grynko.learningcourses.wrapper;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleServiceWrapper {

    private final RoleService roleService;
    private final ModelMapper modelMapper;

    public RoleServiceWrapper(RoleService roleService, ModelMapper modelMapper) {
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    public RoleDto toDto(Role entity) {
        return modelMapper.map(entity, RoleDto.class);
    }

    public Role fromDto(RoleDto dto) {
        return modelMapper.map(dto, Role.class);
    }

}
