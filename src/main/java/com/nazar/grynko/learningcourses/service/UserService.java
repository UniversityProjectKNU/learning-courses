package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserService {

    private final UserInternalService userInternalService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserInternalService userInternalService, RoleService roleService,
                       ModelMapper modelMapper) {
        this.userInternalService = userInternalService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    public Optional<UserDto> get(Long id) {
        return userInternalService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public List<UserDto> getAll() {
        return userInternalService.getAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public void delete(Long id) {
        userInternalService.delete(id);
    }

    public UserDto update(UserDto userDto) {
        User user = fromDto(userDto);
        user = userInternalService.update(user);
        return toDto(user);
    }

    public Set<RoleDto> updateRoles(Set<RoleDto> rolesDto, Long userId) {
        Set<Role> roles = rolesDto.stream().map(roleService::fromDto).collect(Collectors.toSet());

        roles = userInternalService.updateRoles(roles, userId);

        return roles.stream()
                .map(roleService::toDto)
                .collect(Collectors.toSet());
    }

    public Set<RoleDto> getUsersRoles(Long id) {
        return userInternalService.getUsersRoles(id)
                .stream()
                .map(roleService::toDto)
                .collect(Collectors.toSet());
    }

    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User fromDto(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

}
