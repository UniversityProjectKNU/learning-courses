package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoSave;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoUpdate;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    private final ModelMapper modelMapper;

    public CourseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CourseDto toDto(Course entity) {
        return modelMapper.map(entity, CourseDto.class);
    }

    public Course fromDto(CourseDto dto) {
        return modelMapper.map(dto, Course.class);
    }

    public Course fromDtoSave(CourseDtoSave dto) {
        return modelMapper.map(dto, Course.class);
    }

    public Course fromTemplate(CourseTemplate template) {
        return modelMapper.map(template, Course.class);
    }

    public Course fromDtoUpdate(CourseDtoUpdate dto) {
        return modelMapper.map(dto, Course.class);
    }
}
