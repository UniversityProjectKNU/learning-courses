package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
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
@RequestMapping("learning-courses/api/v1/templates/courses")
@RolesAllowed({"INSTRUCTOR", "ADMIN"})
public class CourseTemplateController {

    private final CourseTemplateService courseTemplateService;
    private final ChapterTemplateService chapterTemplateService;
    private final LessonTemplateService lessonTemplateService;
    private final CourseService courseService;

    @Autowired
    public CourseTemplateController(CourseTemplateService courseTemplateService,
                                    ChapterTemplateService chapterTemplateService,
                                    LessonTemplateService lessonTemplateService,
                                    CourseService courseService) {
        this.courseTemplateService = courseTemplateService;
        this.chapterTemplateService = chapterTemplateService;
        this.lessonTemplateService = lessonTemplateService;
        this.courseService = courseService;
    }

    @GetMapping("/course")
    ResponseEntity<CourseTemplateDto> one(@RequestParam Long courseTemplateId) {
        return ResponseEntity.ok(courseTemplateService.get(courseTemplateId)
                .orElseThrow(InvalidPathException::new));
    }

    @GetMapping
    List<CourseTemplateDto> all() {
        return courseTemplateService.getAll();
    }

    @GetMapping("/course/lessons")
    ResponseEntity<List<LessonTemplateDto>> allLessonsInCourseTemplate(@RequestParam Long courseTemplateId) {
        return ResponseEntity.ok(lessonTemplateService.getAllInCourseTemplate(courseTemplateId));
    }

    @GetMapping("/course/chapters")
    ResponseEntity<List<ChapterTemplateDto>> allChaptersInCourseTemplate(@RequestParam Long courseTemplateId) {
        return ResponseEntity.ok(chapterTemplateService.getAllInCourseTemplate(courseTemplateId));
    }

    @DeleteMapping("/course")
    void delete(@RequestParam Long courseTemplateId) {
        courseTemplateService.delete(courseTemplateId);
    }

    @PostMapping
    ResponseEntity<CourseTemplateDto> save(@RequestBody CourseTemplateDtoSave courseTemplateDtoSave) {
        return ResponseEntity.ok(courseTemplateService.save(courseTemplateDtoSave));
    }

    @PutMapping("/course")
    ResponseEntity<CourseTemplateDto> update(@RequestParam Long courseTemplateId, @RequestBody CourseTemplateDtoUpdate courseTemplateDto) {
        if (!courseTemplateDto.getId().equals(courseTemplateId)) {
            throw new IllegalArgumentException();
        }

        return ResponseEntity.ok(courseTemplateService.update(courseTemplateDto));
    }

    @PostMapping("/course/create-instance")
    ResponseEntity<CourseDto> createInstance(@RequestParam Long courseTemplateId, Principal principal) {
        return ResponseEntity.ok(courseService.create(courseTemplateId, principal.getName()));
    }

}
