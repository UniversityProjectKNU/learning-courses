package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.role.UserRoleUpdateDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserDtoUpdate;
import com.nazar.grynko.learningcourses.mapper.UserMapper;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserService {

    private final UserInternalService userInternalService;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserInternalService userInternalService, UserMapper userMapper) {
        this.userInternalService = userInternalService;
        this.userMapper = userMapper;
    }

    public UserDto get(Long id) {
        var user = userInternalService.get(id);
        return userMapper.toDto(user);
    }

    public UserDto get(String login) {
        var user = userInternalService.getByLogin(login);
        return userMapper.toDto(user);
    }

    public List<UserDto> getAll() {
        return userInternalService.getAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long userId) {
        userInternalService.delete(userId);
    }

    public UserDto update(UserDtoUpdate dto, Long userId) {
        var user = userMapper.fromDtoUpdate(dto)
                .setId(userId);
        user = userInternalService.update(user);
        return userMapper.toDto(user);
    }

    public UserDto updateSelf(UserDtoUpdate dto, String login) {
        var userId = userInternalService.getByLogin(login)
                .getId();

        return update(dto, userId);
    }

    public UserDto save(User user) {
        user = userInternalService.save(user);
        return userMapper.toDto(user);
    }

    public UserDto updateRole(UserRoleUpdateDto roleUpdate, Long userId) {
        var user = userInternalService.updateRole(roleUpdate.getType(), userId);
        return userMapper.toDto(user);
    }
}
