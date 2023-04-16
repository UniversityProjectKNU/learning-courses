package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoSave;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CourseTemplateMapper {

    private final ModelMapper modelMapper;

    public CourseTemplateMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CourseTemplate fromDto(CourseTemplateDto dto) {
        return modelMapper.map(dto, CourseTemplate.class);
    }

    public CourseTemplate fromDto(CourseTemplateDtoSave dto) {
        return modelMapper.map(dto, CourseTemplate.class);
    }

    public CourseTemplateDto toDto(CourseTemplate entity) {
        return modelMapper.map(entity, CourseTemplateDto.class);
    }

}
