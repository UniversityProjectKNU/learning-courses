package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.UserServiceWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
        if(!userDto.getId().equals(id)) throw new IllegalArgumentException();

        return serviceWrapper.update(userDto);
    }

    @GetMapping("/{id}/roles")
    Set<RoleDto> getUsersRoles(@PathVariable Long id) {
        return serviceWrapper.getUsersRoles(id);
    }

    @PutMapping("/{id}/roles")
    Set<RoleDto> updateRoles(@RequestBody Set<RoleDto> roles, @PathVariable Long id) {
        if(roles == null || roles.isEmpty()) throw new IllegalArgumentException();
        serviceWrapper.get(id).orElseThrow(InvalidPathException::new);

        return serviceWrapper.updateRoles(roles, id);
    }

}
