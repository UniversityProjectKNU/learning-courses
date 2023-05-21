package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.security.SignInDto;
import com.nazar.grynko.learningcourses.dto.security.SignUpDto;
import com.nazar.grynko.learningcourses.dto.simple.SimpleDto;
import com.nazar.grynko.learningcourses.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("learning-courses/api/v1")
public class SecurityController {

    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInDto) {
        try {
            return ResponseEntity.ok(securityService.signIn(signInDto));
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) {
        try {
            return ResponseEntity.ok(securityService.signUp(signUpDto));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new SimpleDto<>(e.getMessage()));
        }
    }

}
