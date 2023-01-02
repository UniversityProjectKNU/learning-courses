package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.service.CourseTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {
        "courses-templates",
})
public class CourseTemplateController {

    private final CourseTemplateService courseTemplateService;

    @Autowired
    public CourseTemplateController(CourseTemplateService courseTemplateService) {
        this.courseTemplateService = courseTemplateService;
    }

    @GetMapping("/{id}")
    CourseTemplate one(@PathVariable Long id) {
        return courseTemplateService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<CourseTemplate> all() {
        return courseTemplateService.getAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        courseTemplateService.delete(id);
    }

    @PostMapping
    CourseTemplate save(@RequestBody CourseTemplate courseTemplate) {
        return courseTemplateService.save(courseTemplate);
    }

    @PutMapping("/{id}")
    void update(@RequestBody CourseTemplate courseTemplate) {
        courseTemplateService.update(courseTemplate);
    }
    
}
