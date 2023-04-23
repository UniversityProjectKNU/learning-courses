package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("learning-courses/api/v1/user/my-courses")
public class UserCourseController {

    private final CourseService courseService;
    private final LessonService lessonService;

    public UserCourseController(CourseService courseService,
                                LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    @GetMapping
    ResponseEntity<?> getCourses(@RequestParam(required = false) Boolean isFinished,
                                 Principal principal) {
        try {
            return ResponseEntity.ok(courseService.getUsersCourses(principal.getName(), isFinished));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }

    }

    @GetMapping("/{courseId}/info")
    ResponseEntity<?> getCourseInfo(@PathVariable Long courseId, Principal principal) {
        try {
            return ResponseEntity.ok(courseService.getUsersCourseInfo(courseId, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{courseId}/lessons")
    ResponseEntity<?> getCoursesLessons(@PathVariable Long courseId, Principal principal) {
        try {
            return ResponseEntity.ok(lessonService.getUsersLessonsForCourse(courseId, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{courseId}/lessons/{lessonId}/info")
    ResponseEntity<?> getLessonInfo(@PathVariable Long courseId,
                                    @PathVariable Long lessonId,
                                    Principal principal) {
        try {
            if (!lessonService.hasWithCourse(lessonId, courseId)) {
                throw new InvalidPathException(String.format("Lesson %d in course %d doesn't exist", lessonId, courseId));
            }
            return ResponseEntity.ok(lessonService.getUsersLessonInfo(lessonId, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

}
