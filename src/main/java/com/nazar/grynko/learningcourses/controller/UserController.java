package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.dto.role.UserRoleUpdateDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.RoleService;
import com.nazar.grynko.learningcourses.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RolesAllowed("ADMIN")
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
    ResponseEntity<UserDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(userService.get(id)
                .orElseThrow(InvalidPathException::new));
    }

    @GetMapping
    ResponseEntity<List<UserDto>> all() {
        return ResponseEntity.ok(userService.getAll());
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserDto> update(@RequestBody UserDtoUpdate dtoUpdate, @PathVariable Long id) {
        if (!dtoUpdate.getId().equals(id)) {
            throw new IllegalArgumentException();
        }
        return ResponseEntity.ok(userService.update(dtoUpdate, id));
    }

    @GetMapping("/{id}/role")
    ResponseEntity<RoleDto> getUsersRole(@PathVariable Long id) {
        userService.get(id).orElseThrow(InvalidPathException::new);
        return ResponseEntity.ok(roleService.getUsersRoles(id));
    }

    @PutMapping("/{id}/role")
    ResponseEntity<RoleDto> updateRole(@RequestBody UserRoleUpdateDto role, @PathVariable Long id) {
        userService.get(id).orElseThrow(InvalidPathException::new);
        return ResponseEntity.ok(roleService.updateRole(role, id));
    }

}
