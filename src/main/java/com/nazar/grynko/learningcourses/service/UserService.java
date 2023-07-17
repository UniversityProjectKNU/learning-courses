package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserDtoUpdate;
import com.nazar.grynko.learningcourses.mapper.UserMapper;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
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

    public UserDto update(UserDtoUpdate dto) {
        var user = userMapper.fromDtoUpdate(dto);
        user = userInternalService.update(user);
        return userMapper.toDto(user);
    }

    public UserDto save(User user) {
        user = userInternalService.save(user);
        return userMapper.toDto(user);
    }

    public boolean isTheSameUser(Long id, Principal principal) {
        var userByLogin = userInternalService.getByLogin(principal.getName());
        var userById = userInternalService.get(id);

        return userByLogin.isPresent() && userById.isPresent() && Objects.equals(userByLogin.get().getLogin(), userById.get().getLogin());
    }

}
