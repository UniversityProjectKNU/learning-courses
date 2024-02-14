package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.role.UserRoleUpdateDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserDtoCreate;
import com.nazar.grynko.learningcourses.dto.user.UserDtoUpdate;
import com.nazar.grynko.learningcourses.service.SecurityService;
import com.nazar.grynko.learningcourses.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode.*;

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

    @Operation(summary = "Get a user",
            description = "Get a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the user"),
            @ApiResponse(responseCode = _404, description = "User not found")
    })
    @RolesAllowed({"ADMIN", "INSTRUCTOR", "STUDENT"})
    @GetMapping("/user")
    ResponseEntity<UserDto> one(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.get(userId));
    }

    @Operation(summary = "Get all users",
            description = "Get all users in the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the user"),
    })
    @GetMapping
    ResponseEntity<List<UserDto>> all() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(summary = "Create new user",
            description = "Ability of the administrator to create new user with any role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Created a user"),
            @ApiResponse(responseCode = _400, description = "Admin cannot be created"),
            @ApiResponse(responseCode = _401, description = "User or password is incorrect / User already exists")
    })
    @PostMapping
    ResponseEntity<UserDto> createUser(@RequestBody UserDtoCreate dto) {
        return ResponseEntity.ok(securityService.createUser(dto));
    }

    @Operation(summary = "Delete a user",
            description = "Delete a user by its id. But you cannot delete Admin or Instructor with at least one any UserToCourse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "User deleted"),
            @ApiResponse(responseCode = _400, description = "Admin cannot be deleted / Instructor cannot be deleted if they have at least one any UserToCourse"),
            @ApiResponse(responseCode = _404, description = "User not found")
    })
    @DeleteMapping("/user")
    void delete(@RequestParam Long userId) {
        userService.delete(userId);
    }

    @Operation(summary = "Update a user",
            description = "Update user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "User updated"),
            @ApiResponse(responseCode = _404, description = "User not found")
    })
    @PutMapping("/user")
    ResponseEntity<UserDto> update(@RequestParam Long userId,
                                   @Valid @RequestBody UserDtoUpdate dtoUpdate) {
        return ResponseEntity.ok(userService.update(dtoUpdate, userId));
    }

    @Operation(summary = "Update user's role",
            description = "Update user's role by user id. Updates role only in case if user doesn't have any active courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "User's role updated"),
            @ApiResponse(responseCode = _404, description = "User not found")
    })
    @PutMapping("/user/role")
    ResponseEntity<UserDto> updateRole(@RequestParam Long userId,
                                       @Valid @RequestBody UserRoleUpdateDto role) {
        return ResponseEntity.ok(userService.updateRole(role, userId));
    }

}
