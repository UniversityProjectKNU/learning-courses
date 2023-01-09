package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.CourseTemplateServiceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {
        "courses-templates",
})
public class CourseTemplateController {

    private final CourseTemplateServiceWrapper serviceWrapper;

    @Autowired
    public CourseTemplateController(CourseTemplateServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping("/{id}")
    CourseTemplateDto one(@PathVariable Long id) {
        return serviceWrapper.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<CourseTemplateDto> all() {
        return serviceWrapper.getAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        serviceWrapper.delete(id);
    }

    @PostMapping
    CourseTemplateDto save(@RequestBody CourseTemplateDto courseTemplateDto) {
        return serviceWrapper.save(courseTemplateDto);
    }

    @PutMapping("/{id}")
    CourseTemplateDto update(@RequestBody CourseTemplateDto courseTemplateDto, @PathVariable Long id) {
        return serviceWrapper.update(courseTemplateDto, id);
    }
    
}
