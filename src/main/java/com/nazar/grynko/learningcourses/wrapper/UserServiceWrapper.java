package com.nazar.grynko.learningcourses.wrapper;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserServiceWrapper {

    private final UserService userService;
    private final RoleServiceWrapper roleServiceWrapper;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceWrapper(UserService userService, RoleServiceWrapper roleServiceWrapper,
                              ModelMapper modelMapper) {
        this.userService = userService;
        this.roleServiceWrapper = roleServiceWrapper;
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

    public UserDto update(UserDto userDto) {
        User user = fromDto(userDto);
        user = userService.update(user);
        return toDto(user);
    }

    public Set<RoleDto> updateRoles(Set<RoleDto> rolesDto, Long userId) {
        Set<Role> roles = rolesDto.stream().map(roleServiceWrapper::fromDto).collect(Collectors.toSet());

        roles = userService.updateRoles(roles, userId);

        return roles.stream()
                .map(roleServiceWrapper::toDto)
                .collect(Collectors.toSet());
    }

    public Set<RoleDto> getUsersRoles(Long id) {
        return userService.getUsersRoles(id)
                .stream()
                .map(roleServiceWrapper::toDto)
                .collect(Collectors.toSet());
    }

    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User fromDto(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

}
