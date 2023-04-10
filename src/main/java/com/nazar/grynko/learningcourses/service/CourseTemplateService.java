package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateSave;
import com.nazar.grynko.learningcourses.mapper.CourseTemplateMapper;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.service.internal.CourseTemplateInternalService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CourseTemplateService {

    private final CourseTemplateInternalService courseTemplateInternalService;
    private final CourseTemplateMapper courseTemplateMapper;


    public CourseTemplateService(CourseTemplateInternalService courseTemplateInternalService,
                                 CourseTemplateMapper courseTemplateMapper) {
        this.courseTemplateInternalService = courseTemplateInternalService;
        this.courseTemplateMapper = courseTemplateMapper;
    }

    public Optional<CourseTemplateDto> get(Long id) {
        return courseTemplateInternalService.get(id)
                .flatMap(val -> Optional.of(courseTemplateMapper.toDto(val)));
    }

    public List<CourseTemplateDto> getAll() {
        return courseTemplateInternalService.getAll()
                .stream()
                .map(courseTemplateMapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        courseTemplateInternalService.delete(id);
    }

    public CourseTemplateDto save(CourseTemplateSave dto) {
        CourseTemplate entity = courseTemplateMapper.fromDto(dto);
        entity = courseTemplateInternalService.save(entity);
        return courseTemplateMapper.toDto(entity);
    }

    public CourseTemplateDto update(CourseTemplateDto dto) {
        CourseTemplate entity = courseTemplateMapper.fromDto(dto);
        entity = courseTemplateInternalService.update(entity);
        return courseTemplateMapper.toDto(entity);
    }

}
