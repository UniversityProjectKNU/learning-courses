package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.security.SignInDto;
import com.nazar.grynko.learningcourses.dto.security.SignUpDto;
import com.nazar.grynko.learningcourses.dto.simple.SimpleDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserSecurityDto;
import com.nazar.grynko.learningcourses.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._200;
import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._401;

@RestController
@RequestMapping("learning-courses/api/v1")
public class SecurityController {

    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Operation(summary = "Sign in",
            description = "Sign in into the application. Provide login as email and password. Get a JWT token in the reponse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Signed in"),
            @ApiResponse(responseCode = _401, description = "User or password is incorrect")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<UserSecurityDto> signIn(@Valid @RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(securityService.signIn(signInDto));
    }

    @Operation(summary = "Sign up",
            description = "Sign up into the application. Provide login as email and password. Get a ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Signed up"),
            @ApiResponse(responseCode = _401, description = "User or password is incorrect / User already exists")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(securityService.signUp(signUpDto));
    }

    @Operation(summary = "Logout",
            description = "End session by removing cookies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Signed up")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse resp) {
        securityService.logout(req, resp);
        return ResponseEntity.ok(new SimpleDto<>("OK"));
    }

}
