package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateSave;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.CourseServiceWrapper;
import com.nazar.grynko.learningcourses.wrapper.CourseTemplateServiceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses-templates")
public class CourseTemplateController {

    private final CourseTemplateServiceWrapper serviceWrapper;
    private final CourseServiceWrapper courseServiceWrapper;

    @Autowired
    public CourseTemplateController(CourseTemplateServiceWrapper serviceWrapper,
                                    CourseServiceWrapper courseServiceWrapper) {
        this.serviceWrapper = serviceWrapper;
        this.courseServiceWrapper = courseServiceWrapper;
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
    CourseTemplateDto save(@RequestBody CourseTemplateSave courseTemplateSave) {
        return serviceWrapper.save(courseTemplateSave);
    }

    @PutMapping("/{id}")
    CourseTemplateDto update(@RequestBody CourseTemplateDto courseTemplateDto, @PathVariable Long id) {
        if(!courseTemplateDto.getId().equals(id))
            throw new IllegalArgumentException();

        return serviceWrapper.update(courseTemplateDto);
    }

    @PostMapping("/{id}/create")
    CourseDto create(@PathVariable Long id) {
        return courseServiceWrapper.create(id);
    }
    
}
