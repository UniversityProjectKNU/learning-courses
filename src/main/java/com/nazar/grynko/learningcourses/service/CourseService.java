package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.service.internal.CourseInternalService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CourseService {

    private final CourseInternalService courseInternalService;
    private final ModelMapper modelMapper;

    public CourseService(CourseInternalService courseInternalService, ModelMapper modelMapper) {
        this.courseInternalService = courseInternalService;
        this.modelMapper = modelMapper;
    }

    public Optional<CourseDto> get(Long id) {
        return courseInternalService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public List<CourseDto> getAll() {
        return courseInternalService.getAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        courseInternalService.delete(id);
    }

    public CourseDto create(Long courseTemplateId) {
        Course course = courseInternalService.create(courseTemplateId);
        return toDto(course);
    }

    public CourseDto save(CourseDto dto) {
        Course entity = fromDto(dto);
        return toDto(courseInternalService.save(entity));
    }

    public CourseDto update(CourseDto dto, Long courseId) {
        Course entity = fromDto(dto).setId(courseId);
        entity = courseInternalService.update(entity);
        return toDto(entity);
    }

    public CourseDto toDto(Course entity) {
        return modelMapper.map(entity, CourseDto.class);
    }

    public Course fromDto(CourseDto dto) {
        return modelMapper.map(dto, Course.class);
    }

}
