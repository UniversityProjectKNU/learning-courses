package com.nazar.grynko.learningcourses.wrapper;

import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.service.RoleService;
import com.nazar.grynko.learningcourses.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserServiceWrapper {

    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceWrapper(UserService userService, RoleService roleService, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    public Optional<UserDto> get(Long id) {
        return userService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public List<UserDto> getAll() {
        return userService.getAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public void delete(Long id) {
        userService.delete(id);
    }

    public UserDto update(UserDto userDto, Long id) {
        User user = fromDto(userDto).setId(id);

        if(user.getRoles() != null) {
            List<RoleType> types = user.getRoles().stream().map(Role::getType).collect(Collectors.toList());
            List<Role> roles = roleService.getAllByTypeIn(types);
            user.setRoles(new HashSet<>(roles));
        }

        userService.update(user);
        return toDto(user);
    }

    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User fromDto(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

}
