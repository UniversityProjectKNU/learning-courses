package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.mapper.RoleMapper;
import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleService {

    private final RoleMapper roleMapper;
    private final UserInternalService userInternalService;

    public RoleService(RoleMapper roleMapper, UserInternalService userInternalService) {
        this.roleMapper = roleMapper;
        this.userInternalService = userInternalService;
    }


    public Set<RoleDto> updateRoles(Set<RoleDto> rolesDto, Long userId) {
        Set<Role> roles = rolesDto.stream().map(roleMapper::fromDto).collect(Collectors.toSet());

        roles = userInternalService.updateRoles(roles, userId);

        return roles.stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet());
    }

    public Set<RoleDto> getUsersRoles(Long id) {
        return userInternalService.getUsersRoles(id)
                .stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet());
    }

}
