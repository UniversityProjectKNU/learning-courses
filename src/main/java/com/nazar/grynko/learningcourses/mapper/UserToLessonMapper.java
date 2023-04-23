package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.model.UserToLesson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserToLessonMapper {

    private final ModelMapper modelMapper;

    public UserToLessonMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserToLessonDto toDto(UserToLesson entity) {
        return modelMapper.map(entity, UserToLessonDto.class);
    }

    public UserToLesson fromDtoUpdate(UserToCourseDtoUpdate dto) {
        return modelMapper.map(dto, UserToLesson.class);
    }
}
