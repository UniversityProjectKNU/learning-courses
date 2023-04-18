package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.mapper.RoleMapper;
import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.service.internal.RoleInternalService;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleService {

    private final RoleMapper roleMapper;
    private final RoleInternalService roleInternalService;

    public RoleService(RoleMapper roleMapper, RoleInternalService roleInternalService) {
        this.roleMapper = roleMapper;
        this.roleInternalService = roleInternalService;
    }

    public Set<RoleDto> updateRoles(Set<RoleDto> rolesDto, Long userId) {
        Set<Role> roles = rolesDto.stream().map(roleMapper::fromDto).collect(Collectors.toSet());

        return roleInternalService.updateRoles(roles, userId)
                .stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet());
    }

    public Set<RoleDto> getUsersRoles(Long id) {
        return roleInternalService.getUsersRoles(id)
                .stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet());
    }

}
