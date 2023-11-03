package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("learning-courses/api/v1/self")
public class SelfController {

    private final UserService userService;

    public SelfController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<UserDto> getSelf(Principal principal) {
        return ResponseEntity.ok(userService.get(principal.getName())
                .orElseThrow(InvalidPathException::new));
    }

    @PutMapping
    ResponseEntity<UserDto> updateSelf(@RequestBody UserDtoUpdate dtoUpdate, Principal principal) {
        return ResponseEntity.ok(userService.updateSelf(dtoUpdate, principal.getName()));
    }

}
