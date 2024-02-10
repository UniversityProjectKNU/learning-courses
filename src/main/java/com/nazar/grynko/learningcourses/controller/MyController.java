package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._200;
import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._404;

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

    @Operation(summary = "Get a list of all user's courses of the principal",
            description = "Get a list of all courses where user take part. Pass isActive flag to filter courses based on their status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the courses"),
            @ApiResponse(responseCode = _404, description = "User not found")
    })
    @GetMapping("/courses")
    ResponseEntity<List<CourseDto>> getUsersAllCourses(@RequestParam(required = false) Boolean isActive,
                                                       Principal principal) {
        return ResponseEntity.ok(courseService.getAllUsersCourses(principal.getName(), isActive));
    }

    @Operation(summary = "Get a user-course performance of the principal",
            description = "Get a user-course performance of the principal for the specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the course-performance"),
            @ApiResponse(responseCode = _404, description = "User not found / No such User with Course")
    })
    @GetMapping("/courses/course")
    ResponseEntity<UserToCourseDto> getUsersCoursePerformanceInfo(@RequestParam Long courseId,
                                                                  Principal principal) {
        return ResponseEntity.ok(courseService.getUsersCourseInfo(courseId, principal.getName()));
    }

    @Operation(summary = "Get a list of user-lesson performance for the course of the principal",
            description = "Get a list of user-lesson performance for the specific course of the principal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the list of lesson-performance"),
            @ApiResponse(responseCode = _404, description = "Course not found / User not found / No such User with Course")
    })
    @GetMapping("/courses/course/lessons")
    ResponseEntity<List<UserToLessonDto>> getUsersAllLessonPerformanceInfoForCourse(@RequestParam Long courseId,
                                                                                    Principal principal) {
        return ResponseEntity.ok(lessonService.getAllUsersLessonsForCourse(courseId, principal.getName()));
    }

    @Operation(summary = "Get a list of user-lesson performance for the chapter of the principal",
            description = "Get a list of user-lesson performance for the specific chapter of the principal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the list of lesson-performance"),
            @ApiResponse(responseCode = _404, description = "Chapter not found / User not found / No such User with Course")
    })
    @GetMapping("/chapters/chapter/lessons")
    ResponseEntity<List<UserToLessonDto>> getUsersAllLessonPerformanceInfoForChapter(@RequestParam Long chapterId,
                                                                                     Principal principal) {
        return ResponseEntity.ok(lessonService.getAllLessonsOfOneUserForChapter(chapterId, principal.getName()));
    }

    @Operation(summary = "Get a user-lesson performance of the principal",
            description = "Get a user-lesson performance of the principal for the specific lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the lesson-performance"),
            @ApiResponse(responseCode = _404, description = "Lesson not found / User not found / No such User with Lesson")
    })
    @GetMapping("/lessons/lesson")
    ResponseEntity<UserToLessonDto> getUserLessonPerformanceInfo(@RequestParam Long lessonId,
                                                                 Principal principal) {
        return ResponseEntity.ok(lessonService.getUsersLessonInfoByLogin(lessonId, principal.getName()));
    }

}
