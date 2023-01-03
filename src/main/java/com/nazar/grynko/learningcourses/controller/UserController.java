package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.json.JsonArg;
import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
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
    void updatePublicInformation(@RequestBody User user) {
        userService.updatePublicInformation(user);
    }

    @PutMapping("/{id}/roles")
    void updateRoles(@JsonArg("user") User user, @JsonArg("roles") HashSet<Role> roles) {
        userService.updateRoles(user, new HashSet<>(roles));
    }

}
