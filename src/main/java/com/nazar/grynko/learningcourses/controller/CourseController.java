package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoSave;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseInfoDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.RoleType;
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
    private final LessonService lessonService;

    @Autowired
    public CourseController(CourseService courseService,
                            LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    @GetMapping("/{id}")
    ResponseEntity<CourseDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.get(id)
                .orElseThrow(InvalidPathException::new));
    }

    @RolesAllowed("ADMIN")
    @GetMapping
    ResponseEntity<List<CourseDto>> all() {
        return ResponseEntity.ok(courseService.getAll());
    }

    @GetMapping("/{id}/lessons")
    ResponseEntity<List<LessonDto>> allLessonsInCourse(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getAllInCourse(id));
    }

    //TODO use id or refactor LessonController
    @GetMapping("/{id}/lessons/{lessonId}")
    ResponseEntity<LessonDto> getLessonsInCourse(@PathVariable Long id, @PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.get(lessonId)
                .orElseThrow(IllegalArgumentException::new));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        courseService.delete(id);
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PostMapping
    ResponseEntity<CourseDto> save(@RequestBody CourseDtoSave courseDto) {
        return ResponseEntity.ok(courseService.save(courseDto));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}")
    ResponseEntity<CourseDto> update(@RequestBody CourseDtoUpdate courseDto, @PathVariable Long id) {
        if (!Objects.equals(courseDto.getId(), id)) {
            throw new InvalidPathException();
        }

        return ResponseEntity.ok(courseService.update(courseDto, id));
    }

    //TODO if course is finished - no actions with it
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}/finish")
    ResponseEntity<CourseDto> finish(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.finish(id));
    }

    @RolesAllowed("STUDENT")
    @PostMapping("/{id}/enroll")
    ResponseEntity<UserToCourseDto> enroll(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(courseService.enroll(id, principal.getName()));
    }

    @GetMapping("/{id}/users")
    ResponseEntity<List<UserToCourseInfoDto>> allUsersForCourse(@PathVariable Long id, @RequestParam(required = false) RoleType roleType) {
        return ResponseEntity.ok(courseService.getAllUserToCourseInfoForCourse(id, roleType));
    }

    @RolesAllowed("ADMIN")
    @PutMapping("/{id}/users/instructors")
    ResponseEntity<UserToCourseDto> assignInstructor(@PathVariable Long id, @RequestParam Long instructorId) {
        return ResponseEntity.ok(courseService.assignInstructor(id, instructorId));
    }

    //TODO the same as UserCourseController#getCoursesLessons
    //If it's not instructor's student
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @GetMapping("/{id}/users/{userId}")
    ResponseEntity<UserToCourseDto> getUserToCourseInfo(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok(courseService.getUsersCourseInfo(id, userId));
    }

    //TODO think if we need to move it to UserCourseController
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}/users/{userId}")
    ResponseEntity<UserToCourseDto> updateUsersCourseInfo(@PathVariable Long id,
                                            @PathVariable Long userId,
                                            @RequestBody UserToCourseDtoUpdate userToCourseDto) {
        return ResponseEntity.ok(courseService.updateUserToCourse(id, userId, userToCourseDto));
    }

    @GetMapping("/{id}/lessons/{lessonId}/users/{userId}")
    ResponseEntity<UserToLessonDto> getStudentLessonInfo(@PathVariable Long id, @PathVariable Long lessonId,
                                                         @PathVariable Long userId) {
        if (!lessonService.hasWithCourse(lessonId, id)) {
            throw new InvalidPathException(String.format("Lesson %d in course %d doesn't exist", lessonId, id));
        }
        return ResponseEntity.ok(lessonService.getStudentLessonInfo(lessonId, userId));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}/lessons/{lessonId}/users/{userId}")
    ResponseEntity<UserToLessonDto> updateUsersLessonInfo(@PathVariable Long id,
                                            @PathVariable Long lessonId,
                                            @PathVariable Long userId,
                                            @RequestBody UserToCourseDtoUpdate userToCourseDto) {
        if (!lessonService.hasWithCourse(lessonId, id)) {
            throw new InvalidPathException(String.format("Lesson %d in course %d doesn't exist", lessonId, id));
        }
        return ResponseEntity.ok(lessonService.updateUserToLesson(lessonId, userId, userToCourseDto));
    }

}
