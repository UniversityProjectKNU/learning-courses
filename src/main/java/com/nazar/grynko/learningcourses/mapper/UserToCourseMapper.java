package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseInfoDto;
import com.nazar.grynko.learningcourses.model.UserToCourse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserToCourseMapper {

    private final ModelMapper modelMapper;

    public UserToCourseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserToCourseDto toDto(UserToCourse entity) {
        return modelMapper.map(entity, UserToCourseDto.class);
    }

    public UserToCourse fromDtoUpdate(UserToCourseDtoUpdate dto) {
        return modelMapper.map(dto, UserToCourse.class);
    }

    public UserToCourseInfoDto toUserToCourseInfoDto(UserToCourse entity) {
        return modelMapper.map(entity, UserToCourseInfoDto.class);
    }
}
