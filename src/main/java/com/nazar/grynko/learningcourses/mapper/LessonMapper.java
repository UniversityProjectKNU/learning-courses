package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.model.Lesson;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    private final ModelMapper modelMapper;

    public LessonMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public LessonDto toDto(Lesson entity) {
        return modelMapper.map(entity, LessonDto.class);
    }

    public Lesson fromDto(LessonDto dto) {
        return modelMapper.map(dto, Lesson.class);
    }

    public Lesson fromTemplate(LessonTemplate template) {
        return modelMapper.map(template, Lesson.class);
    }

}
