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

    @GetMapping("/user")
    ResponseEntity<UserDto> one(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.get(userId)
                .orElseThrow(InvalidPathException::new));
    }

    @GetMapping
    ResponseEntity<List<UserDto>> all() {
        return ResponseEntity.ok(userService.getAll());
    }

    @DeleteMapping("/user")
    void delete(@RequestParam Long userId) {
        userService.delete(userId);
    }

    @PutMapping("/user")
    ResponseEntity<UserDto> update(@RequestBody UserDtoUpdate dtoUpdate, @RequestParam Long userId) {
        if (!dtoUpdate.getId().equals(userId)) {
            throw new IllegalArgumentException();
        }
        return ResponseEntity.ok(userService.update(dtoUpdate, userId));
    }

    @GetMapping("/user/role")
    ResponseEntity<RoleDto> getUsersRole(@RequestParam Long userId) {
        userService.get(userId).orElseThrow(InvalidPathException::new);
        return ResponseEntity.ok(roleService.getUsersRoles(userId));
    }

    @PutMapping("/user/role")
    ResponseEntity<RoleDto> updateRole(@RequestParam Long userId, @RequestBody UserRoleUpdateDto role) {
        userService.get(userId).orElseThrow(InvalidPathException::new);
        return ResponseEntity.ok(roleService.updateRole(role, userId));
    }

    //TODO create instructor by admin (create user in general)

}
