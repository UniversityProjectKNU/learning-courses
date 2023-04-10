package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateSave;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LessonTemplateMapper {

    private final ModelMapper modelMapper;

    public LessonTemplateMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public LessonTemplate fromDto(LessonTemplateDto dto) {
        return modelMapper.map(dto, LessonTemplate.class);
    }

    public LessonTemplate fromDto(LessonTemplateSave dto) {
        return modelMapper.map(dto, LessonTemplate.class);
    }

    public LessonTemplateDto toDto(LessonTemplate entity) {
        return modelMapper.map(entity, LessonTemplateDto.class);
    }

}
