package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.CourseServiceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses")
public class CourseController {

    private final CourseServiceWrapper serviceWrapper;

    @Autowired
    public CourseController(CourseServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping("/{id}")
    CourseDto one(@PathVariable Long id) {
        return serviceWrapper.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<CourseDto> all() {
        return serviceWrapper.getAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        serviceWrapper.delete(id);
    }

}
