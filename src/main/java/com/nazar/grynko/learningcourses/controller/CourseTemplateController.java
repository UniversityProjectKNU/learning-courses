package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoSave;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.CourseTemplateService;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("learning-courses/api/v1/courses-templates")
public class CourseTemplateController {

    private final CourseTemplateService courseTemplateService;
    private final LessonTemplateService lessonTemplateService;
    private final CourseService courseService;

    @Autowired
    public CourseTemplateController(CourseTemplateService courseTemplateService,
                                    LessonTemplateService lessonTemplateService,
                                    CourseService courseService) {
        this.courseTemplateService = courseTemplateService;
        this.lessonTemplateService = lessonTemplateService;
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

    @GetMapping("/{id}/lessons-templates")
    ResponseEntity<?> allLessonsInCourseTemplate(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(lessonTemplateService.getAllInCourseTemplate(id));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
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
        if (!courseTemplateDto.getId().equals(id)) {
            throw new IllegalArgumentException();
        }

        return courseTemplateService.update(courseTemplateDto);
    }

    @RolesAllowed("INSTRUCTOR")
    @PostMapping("/{id}/create")
    ResponseEntity<?> create(@PathVariable Long id, Principal principal) {
        try {
            return ResponseEntity.ok(courseService.create(id, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

}
