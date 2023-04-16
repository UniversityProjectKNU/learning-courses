package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.mapper.UserMapper;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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

    public Optional<UserDto> get(Long id) {
        return userInternalService.get(id)
                .flatMap(val -> Optional.of(userMapper.toDto(val)));
    }

    public List<UserDto> getAll() {
        return userInternalService.getAll()
                .stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    public void delete(Long id) {
        userInternalService.delete(id);
    }

    //TODO: create new dto for update without login and dateOfBirth
    public UserDto update(UserDto dto) {
        var user = userMapper.fromDto(dto);
        user = userInternalService.update(user);
        return userMapper.toDto(user);
    }

    public UserDto save(User user) {
        user = userInternalService.save(user);
        return userMapper.toDto(user);
    }

    public boolean userExists(String login) {
        return userInternalService.existsUser(login);
    }

}
