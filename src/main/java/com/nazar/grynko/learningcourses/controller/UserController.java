package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.RoleService;
import com.nazar.grynko.learningcourses.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("learning-courses/api/v1/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService,
                          RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    UserDto one(@PathVariable Long id) {
        return userService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<UserDto> all() {
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    UserDto update(@RequestBody UserDtoUpdate dtoUpdate, @PathVariable Long id) {
        if (!dtoUpdate.getId().equals(id)) {
            throw new IllegalArgumentException();
        }

        return userService.update(dtoUpdate);
    }

    @GetMapping("/{id}/roles")
    Set<RoleDto> getUsersRoles(@PathVariable Long id) {
        return roleService.getUsersRoles(id);
    }

    @PutMapping("/{id}/roles")
    Set<RoleDto> updateRoles(@RequestBody Set<RoleDto> roles, @PathVariable Long id) {
        userService.get(id).orElseThrow(InvalidPathException::new);

        return roleService.updateRoles(roles, id);
    }

}
