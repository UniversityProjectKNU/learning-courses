package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoSave;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("learning-courses/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    CourseDto one(@PathVariable Long id) {
        return courseService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<CourseDto> all() {
        return courseService.getAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        courseService.delete(id);
    }

    @PostMapping
    CourseDto save(@RequestBody CourseDtoSave courseDto) {
        return courseService.save(courseDto);
    }

    @PutMapping("/{id}")
    CourseDto update(@RequestBody CourseDto courseDto, @PathVariable Long id) {
        if (!Objects.equals(courseDto.getId(), id))
            throw new InvalidPathException();

        return courseService.update(courseDto, id);
    }

}
