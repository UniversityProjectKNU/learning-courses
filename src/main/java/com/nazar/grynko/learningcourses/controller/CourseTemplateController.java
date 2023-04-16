package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoSave;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.CourseTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses-templates")
public class CourseTemplateController {

    private final CourseTemplateService courseTemplateService;
    private final CourseService courseService;

    @Autowired
    public CourseTemplateController(CourseTemplateService courseTemplateService,
                                    CourseService courseService) {
        this.courseTemplateService = courseTemplateService;
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    CourseTemplateDto one(@PathVariable Long id) {
        return courseTemplateService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<CourseTemplateDto> all() {
        return courseTemplateService.getAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        courseTemplateService.delete(id);
    }

    @PostMapping
    CourseTemplateDto save(@RequestBody CourseTemplateDtoSave courseTemplateDtoSave) {
        return courseTemplateService.save(courseTemplateDtoSave);
    }

    @PutMapping("/{id}")
    CourseTemplateDto update(@RequestBody CourseTemplateDto courseTemplateDto, @PathVariable Long id) {
        if (!courseTemplateDto.getId().equals(id))
            throw new IllegalArgumentException();

        return courseTemplateService.update(courseTemplateDto);
    }

    @PostMapping("/{id}/create")
    CourseDto create(@PathVariable Long id) {
        return courseService.create(id);
    }

}
