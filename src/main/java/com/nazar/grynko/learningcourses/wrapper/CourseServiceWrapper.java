package com.nazar.grynko.learningcourses.wrapper;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CourseServiceWrapper {

    private final CourseService courseService;
    private final ModelMapper modelMapper;

    public CourseServiceWrapper(CourseService courseService, ModelMapper modelMapper) {
        this.courseService = courseService;
        this.modelMapper = modelMapper;
    }

    public Optional<CourseDto> get(Long id) {
        return courseService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public List<CourseDto> getAll() {
        return courseService.getAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        courseService.delete(id);
    }

    public CourseDto create(Long courseTemplateId) {
        Course course = courseService.create(courseTemplateId);
        return toDto(course);
    }

    public CourseDto toDto(Course entity) {
        return modelMapper.map(entity, CourseDto.class);
    }

    public Course fromDto(CourseDto dto) {
        return modelMapper.map(dto, Course.class);
    }

}
