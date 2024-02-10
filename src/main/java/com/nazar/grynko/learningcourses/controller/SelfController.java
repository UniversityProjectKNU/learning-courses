package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserDtoUpdate;
import com.nazar.grynko.learningcourses.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._200;
import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._404;

@RestController
@RequestMapping("learning-courses/api/v1/self")
public class SelfController {

    private final UserService userService;

    public SelfController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user's information for principal",
            description = "Principal can get their information. Usually used to view personal information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found user information"),
            @ApiResponse(responseCode = _404, description = "User not found")
    })
    @GetMapping
    ResponseEntity<UserDto> getSelf(Principal principal) {
        return ResponseEntity.ok(userService.get(principal.getName()));
    }

    @Operation(summary = "Update user's information for principal",
            description = "Principal can update their information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "User information updated"),
            @ApiResponse(responseCode = _404, description = "User not found")
    })
    @PutMapping
    ResponseEntity<UserDto> updateSelf(@Valid @RequestBody UserDtoUpdate dtoUpdate,
                                       Principal principal) {
        return ResponseEntity.ok(userService.updateSelf(dtoUpdate, principal.getName()));
    }

}
