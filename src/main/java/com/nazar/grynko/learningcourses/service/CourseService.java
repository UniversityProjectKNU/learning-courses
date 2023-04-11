package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.mapper.CourseMapper;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.service.internal.CourseInternalService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class CourseService {

    private final CourseInternalService courseInternalService;
    private final CourseMapper courseMapper;

    public CourseService(CourseInternalService courseInternalService, CourseMapper courseMapper) {
        this.courseInternalService = courseInternalService;
        this.courseMapper = courseMapper;
    }

    public Optional<CourseDto> get(Long id) {
        return courseInternalService.get(id)
                .flatMap(val -> Optional.of(courseMapper.toDto(val)));
    }

    public List<CourseDto> getAll() {
        return courseInternalService.getAll()
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        courseInternalService.delete(id);
    }

    public CourseDto create(Long courseTemplateId) {
        Course course = courseInternalService.create(courseTemplateId);
        return courseMapper.toDto(course);
    }

    public CourseDto save(CourseDto dto) {
        if (isNull(dto.getIsFinished()) || !dto.getIsFinished()) {
            throw new IllegalStateException();
        }

        Course entity = courseMapper.fromDto(dto);
        return courseMapper.toDto(courseInternalService.save(entity));
    }

    public CourseDto update(CourseDto dto, Long courseId) {
        Course entity = courseMapper.fromDto(dto).setId(courseId);
        entity = courseInternalService.update(entity);
        return courseMapper.toDto(entity);
    }

}
