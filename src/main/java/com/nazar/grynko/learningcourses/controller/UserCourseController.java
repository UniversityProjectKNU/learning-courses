package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("learning-courses/api/v1/my-courses")
public class UserCourseController {

    private final CourseService courseService;
    private final LessonService lessonService;

    public UserCourseController(CourseService courseService,
                                LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    @GetMapping
    ResponseEntity<List<CourseDto>> getCourses(@RequestParam(required = false) Boolean isFinished,
                                               Principal principal) {
        return ResponseEntity.ok(courseService.getUsersCourses(principal.getName(), isFinished));
    }

    @GetMapping("/{courseId}/info")
    ResponseEntity<UserToCourseDto> getCourseInfo(@PathVariable Long courseId, Principal principal) {
            return ResponseEntity.ok(courseService.getUsersCourseInfo(courseId, principal.getName()));
    }

    @GetMapping("/{courseId}/lessons")
    ResponseEntity<List<UserToLessonDto>> getCoursesLessons(@PathVariable Long courseId, Principal principal) {
        return ResponseEntity.ok(lessonService.getAllUserToLessonForCourse(courseId, principal.getName()));
    }

    @GetMapping("/{courseId}/lessons/{lessonId}/info")
    ResponseEntity<UserToLessonDto> getLessonInfo(@PathVariable Long courseId,
                                    @PathVariable Long lessonId,
                                    Principal principal) {
        if (!lessonService.hasWithCourse(lessonId, courseId)) {
            throw new InvalidPathException(String.format("Lesson %d in course %d doesn't exist", lessonId, courseId));
        }
        return ResponseEntity.ok(lessonService.getUsersLessonInfo(lessonId, principal.getName()));
    }

}
