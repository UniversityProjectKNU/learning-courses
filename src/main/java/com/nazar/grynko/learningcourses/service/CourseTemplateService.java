package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateSave;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.service.internal.CourseTemplateInternalService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CourseTemplateService {
    
    private final CourseTemplateInternalService courseTemplateInternalService;
    private final ModelMapper modelMapper;

    public CourseTemplateService(CourseTemplateInternalService courseTemplateInternalService, ModelMapper modelMapper) {
        this.courseTemplateInternalService = courseTemplateInternalService;
        this.modelMapper = modelMapper;
    }

    public Optional<CourseTemplateDto> get(Long id) {
        return courseTemplateInternalService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }
    
    public List<CourseTemplateDto> getAll() {
        return courseTemplateInternalService.getAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public void delete(Long id) {
        courseTemplateInternalService.delete(id);
    }
    
    public CourseTemplateDto save(CourseTemplateSave dto) {
        CourseTemplate entity = fromDto(dto);
        entity = courseTemplateInternalService.save(entity);
        return toDto(entity);
    }

    public CourseTemplateDto update(CourseTemplateDto dto) {
        CourseTemplate entity = fromDto(dto);
        entity = courseTemplateInternalService.update(entity);
        return toDto(entity);
    }

    private CourseTemplate fromDto(CourseTemplateDto dto) {
        return modelMapper.map(dto, CourseTemplate.class);
    }

    private CourseTemplate fromDto(CourseTemplateSave dto) {
        return modelMapper.map(dto, CourseTemplate.class);
    }

    private CourseTemplateDto toDto(CourseTemplate entity) {
        return modelMapper.map(entity, CourseTemplateDto.class);
    }

}
