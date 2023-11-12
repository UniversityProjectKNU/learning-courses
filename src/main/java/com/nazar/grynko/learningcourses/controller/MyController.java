package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("learning-courses/api/v1/my")
public class MyController {

    private final CourseService courseService;
    private final LessonService lessonService;

    public MyController(CourseService courseService,
                        LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    @GetMapping("/courses")
    ResponseEntity<List<CourseDto>> getCourses(@RequestParam(required = false) Boolean isFinished,
                                               Principal principal) {
        return ResponseEntity.ok(courseService.getUsersCourses(principal.getName(), isFinished));
    }

    @GetMapping("/courses/course")
    ResponseEntity<UserToCourseDto> getCourseInfo(@RequestParam Long courseId, Principal principal) {
            return ResponseEntity.ok(courseService.getUsersCourseInfo(courseId, principal.getName()));
    }

    @GetMapping("/courses/course/lessons")
    ResponseEntity<List<UserToLessonDto>> getAllUsersLessonsForCourse(@RequestParam Long courseId, Principal principal) {
        return ResponseEntity.ok(lessonService.getAllUsersLessonsForCourse(courseId, principal.getName()));
    }

    @GetMapping("/chapters/chapter/lessons")
    ResponseEntity<List<UserToLessonDto>> getAllUsersLessonsInChapter(
            @RequestParam Long chapterId,
            Principal principal) {
        return ResponseEntity.ok(lessonService.getAllLessonsOfOneUserInChapter(chapterId, principal.getName()));
    }

    @GetMapping("/lessons/lesson")
    ResponseEntity<UserToLessonDto> getLessonInfo(
                                    @RequestParam Long lessonId,
                                    Principal principal) {
        return ResponseEntity.ok(lessonService.getUsersLessonInfoByLogin(lessonId, principal.getName()));
    }

}
