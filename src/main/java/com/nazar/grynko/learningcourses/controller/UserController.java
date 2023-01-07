package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    User one(@PathVariable Long id) {
        return userService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<User> all() {
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PostMapping
    User save(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    User update(@RequestBody User user, @PathVariable Long id) {
        userService.validateAndSetId(user, id);
        return userService.update(user);
    }

}
