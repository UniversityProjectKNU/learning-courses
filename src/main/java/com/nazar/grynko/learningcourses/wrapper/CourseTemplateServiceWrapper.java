package com.nazar.grynko.learningcourses.wrapper;

import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.service.CourseTemplateService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CourseTemplateServiceWrapper {
    
    private final CourseTemplateService courseTemplateService;
    private final ModelMapper modelMapper;

    public CourseTemplateServiceWrapper(CourseTemplateService courseTemplateService, ModelMapper modelMapper) {
        this.courseTemplateService = courseTemplateService;
        this.modelMapper = modelMapper;
    }

    public Optional<CourseTemplateDto> get(Long id) {
        return courseTemplateService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }
    
    public List<CourseTemplateDto> getAll() {
        return courseTemplateService.getAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public void delete(Long id) {
        courseTemplateService.delete(id);
    }
    
    public CourseTemplateDto save(CourseTemplateDto dto) {
        CourseTemplate entity = fromDto(dto);
        entity = courseTemplateService.save(entity);
        return toDto(entity);
    }

    public CourseTemplateDto update(CourseTemplateDto dto, Long id) {
        CourseTemplate entity = fromDto(dto).setId(id);
        entity = courseTemplateService.update(entity);
        return toDto(entity);
    }

    private CourseTemplate fromDto(CourseTemplateDto dto) {
        return modelMapper.map(dto, CourseTemplate.class);
    }

    private CourseTemplateDto toDto(CourseTemplate entity) {
        return modelMapper.map(entity, CourseTemplateDto.class);
    }

}
