package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.security.SignUpDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserDtoUpdate;
import com.nazar.grynko.learningcourses.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User fromDto(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    public User fromDtoUpdate(UserDtoUpdate dto) {
        return modelMapper.map(dto, User.class);
    }

    public User fromDtoSave(SignUpDto dto) {
        return modelMapper.map(dto, User.class);
    }
}
