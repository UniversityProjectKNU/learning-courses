package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.dto.role.UserRoleUpdateDto;
import com.nazar.grynko.learningcourses.mapper.RoleMapper;
import com.nazar.grynko.learningcourses.service.internal.RoleInternalService;
import org.springframework.stereotype.Component;


@Component
public class RoleService {

    private final RoleMapper roleMapper;
    private final RoleInternalService roleInternalService;

    public RoleService(RoleMapper roleMapper, RoleInternalService roleInternalService) {
        this.roleMapper = roleMapper;
        this.roleInternalService = roleInternalService;
    }

    public RoleDto updateRole(UserRoleUpdateDto roleUpdate, Long userId) {
        var newRole = roleInternalService.updateRole(roleUpdate.getType(), userId);
        return roleMapper.toDto(newRole);
    }

    public RoleDto getUsersRoles(Long id) {
        var newRole = roleInternalService.getUsersRole(id);
        return roleMapper.toDto(newRole);
    }

}
