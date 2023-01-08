package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.UserServiceWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserServiceWrapper serviceWrapper;

    public UserController(UserServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping("/{id}")
    UserDto one(@PathVariable Long id) {
        return serviceWrapper.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<UserDto> all() {
        return serviceWrapper.getAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        serviceWrapper.delete(id);
    }

    @PutMapping("/{id}")
    UserDto update(@RequestBody UserDto userDto, @PathVariable Long id) {
        return serviceWrapper.update(userDto, id);
    }

}
