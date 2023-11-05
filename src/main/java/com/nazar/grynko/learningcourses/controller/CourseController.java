package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoSave;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseInfoDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.service.ChapterService;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("learning-courses/api/v1/courses")
public class CourseController {

    private final CourseService courseService;
    private final ChapterService chapterService;
    private final LessonService lessonService;

    @Autowired
    public CourseController(CourseService courseService,
                            ChapterService chapterService,
                            LessonService lessonService) {
        this.courseService = courseService;
        this.chapterService = chapterService;
        this.lessonService = lessonService;
    }

    @GetMapping("/course")
    ResponseEntity<CourseDto> one(@RequestParam Long courseId) {
        return ResponseEntity.ok(courseService.get(courseId)
                .orElseThrow(InvalidPathException::new));
    }

    @GetMapping
    ResponseEntity<List<CourseDto>> all(@RequestParam(required = false) Boolean isActive) {
        return ResponseEntity.ok(courseService.getAll(isActive));
    }

    @GetMapping("/course/lessons")
    ResponseEntity<List<LessonDto>> allLessonsInCourse(@RequestParam Long courseId) {
        return ResponseEntity.ok(lessonService.getAllInCourse(courseId));
    }

    @GetMapping("/course/chapters")
    ResponseEntity<List<ChapterDto>> allInCourse(@RequestParam Long courseId) {
        return ResponseEntity.ok(chapterService.getAllInCourse(courseId));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @DeleteMapping("/course")
    void delete(@RequestParam Long courseId) {
        courseService.delete(courseId);
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PostMapping
    ResponseEntity<CourseDto> save(@RequestBody CourseDtoSave courseDto) {
        return ResponseEntity.ok(courseService.save(courseDto));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/course")
    ResponseEntity<CourseDto> update(@RequestBody CourseDtoUpdate courseDto, @RequestParam Long courseId) {
        if (!Objects.equals(courseDto.getId(), courseId)) {
            throw new InvalidPathException();
        }

        return ResponseEntity.ok(courseService.update(courseDto, courseId));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/course/finish")
    ResponseEntity<CourseDto> finish(@RequestParam Long courseId) {
        return ResponseEntity.ok(courseService.finish(courseId));
    }

    //TODO instructor enroll
    //approval
    @RolesAllowed("STUDENT")
    @PostMapping("/course/enroll")
    ResponseEntity<UserToCourseDto> enroll(@RequestParam Long courseId, Principal principal) {
        return ResponseEntity.ok(courseService.enroll(courseId, principal.getName()));
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/course/users/enroll")
    ResponseEntity<UserToCourseDto> enrollWithoutApproval(@RequestParam Long courseId, @RequestParam Long userId) {
        return ResponseEntity.ok(courseService.enrollWithoutApproval(courseId, userId));
    }

    // It meant to return information for all users and only small part of it (only users), but it was very handy for general use.
    // Probably we should separate getAllUsersForCourse and getAllUsersToCourseInfo
    @GetMapping("/course/users/info")
    ResponseEntity<List<UserToCourseInfoDto>> allInfoOfUsersInCourse(@RequestParam Long courseId, @RequestParam(required = false) RoleType roleType) {
        return ResponseEntity.ok(courseService.getAllUserToCourseInfoForCourse(courseId, roleType));
    }

    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @GetMapping("/course/users/user/info")
    ResponseEntity<UserToCourseDto> getUsersCourseInfo(@RequestParam Long courseId, @RequestParam Long userId) {
        return ResponseEntity.ok(courseService.getUsersCourseInfo(courseId, userId));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/course/users/user/info")
    ResponseEntity<UserToCourseDto> updateUsersCourseInfo(@RequestParam Long courseId,
                                            @RequestParam Long userId,
                                            @RequestBody UserToCourseDtoUpdate userToCourseDto) {
        return ResponseEntity.ok(courseService.updateUserToCourse(courseId, userId, userToCourseDto));
    }

}
