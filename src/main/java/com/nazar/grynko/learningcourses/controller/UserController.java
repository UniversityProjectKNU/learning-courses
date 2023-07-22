package com.nazar.grynko.learningcourses.controller;

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

    @RolesAllowed("ADMIN")
    @GetMapping
    List<UserDto> all() {
        return userService.getAll();
    }

    @RolesAllowed("ADMIN")
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

    @RolesAllowed("ADMIN")
    @GetMapping("/{id}/role")
    ResponseEntity<?> getUsersRole(@PathVariable Long id) {
        try {
            userService.get(id).orElseThrow(InvalidPathException::new);
            return ResponseEntity.ok(roleService.getUsersRoles(id));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @RolesAllowed("ADMIN")
    @PutMapping("/{id}/role")
    ResponseEntity<?> updateRole(@RequestBody UserRoleUpdateDto role, @PathVariable Long id) {
        try {
            userService.get(id).orElseThrow(InvalidPathException::new);
            return ResponseEntity.ok(roleService.updateRole(role, id));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

}
