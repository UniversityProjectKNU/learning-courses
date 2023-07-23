package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
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
@RolesAllowed({"INSTRUCTOR", "ADMIN"})
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
    ResponseEntity<CourseTemplateDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(courseTemplateService.get(id)
                .orElseThrow(InvalidPathException::new));
    }

    @GetMapping
    List<CourseTemplateDto> all() {
        return courseTemplateService.getAll();
    }

    @GetMapping("/{id}/lessons-templates")
    ResponseEntity<List<LessonTemplateDto>> allLessonsInCourseTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(lessonTemplateService.getAllInCourseTemplate(id));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        courseTemplateService.delete(id);
    }

    @PostMapping
    ResponseEntity<CourseTemplateDto> save(@RequestBody CourseTemplateDtoSave courseTemplateDtoSave) {
        return ResponseEntity.ok(courseTemplateService.save(courseTemplateDtoSave));
    }

    @PutMapping("/{id}")
    ResponseEntity<CourseTemplateDto> update(@RequestBody CourseTemplateDtoUpdate courseTemplateDto, @PathVariable Long id) {
        if (!courseTemplateDto.getId().equals(id)) {
            throw new IllegalArgumentException();
        }

        return ResponseEntity.ok(courseTemplateService.update(courseTemplateDto));
    }

    @PostMapping("/{id}/create")
    ResponseEntity<CourseDto> create(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(courseService.create(id, principal.getName()));
    }

}
