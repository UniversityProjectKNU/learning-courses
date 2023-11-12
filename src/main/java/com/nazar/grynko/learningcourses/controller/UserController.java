package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.role.UserRoleUpdateDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserDtoCreate;
import com.nazar.grynko.learningcourses.dto.user.UserDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.SecurityService;
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
    private final SecurityService securityService;

    public UserController(UserService userService,
                          SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @RolesAllowed({"INSTRUCTOR", "STUDENT"})
    @GetMapping("/user")
    ResponseEntity<UserDto> one(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.get(userId)
                .orElseThrow(InvalidPathException::new));
    }

    @GetMapping
    ResponseEntity<List<UserDto>> all() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/user")
    ResponseEntity<UserDto> createUser(@RequestBody UserDtoCreate dto) {
        return ResponseEntity.ok(securityService.createUser(dto));
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

    @PutMapping("/user/role")
    ResponseEntity<UserDto> updateRole(@RequestParam Long userId, @RequestBody UserRoleUpdateDto role) {
        userService.get(userId).orElseThrow(InvalidPathException::new);
        return ResponseEntity.ok(userService.updateRole(role, userId));
    }

}
