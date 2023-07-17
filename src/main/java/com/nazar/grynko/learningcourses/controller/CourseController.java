package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoSave;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDtoUpdate;
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
    CourseDto one(@PathVariable Long id) {
        return courseService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @RolesAllowed("ADMIN")
    @GetMapping
    List<CourseDto> all() {
        return courseService.getAll();
    }

    @GetMapping("/{id}/lessons")
    ResponseEntity<?> allLessonsInCourse(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(lessonService.getAllInCourse(id));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    //TODO use id or refactor LessonController
    @GetMapping("/{id}/lessons/{lessonId}")
    ResponseEntity<?> getLessonsInCourse(@PathVariable Long id, @PathVariable Long lessonId) {
        try {
            return ResponseEntity.ok(lessonService.get(lessonId));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        courseService.delete(id);
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PostMapping
    CourseDto save(@RequestBody CourseDtoSave courseDto) {
        return courseService.save(courseDto);
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}")
    CourseDto update(@RequestBody CourseDtoUpdate courseDto, @PathVariable Long id) {
        if (!Objects.equals(courseDto.getId(), id)) {
            throw new InvalidPathException();
        }

        return courseService.update(courseDto, id);
    }

    //TODO if course is finished - no actions with it
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}/finish")
    ResponseEntity<?> finish(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(courseService.finish(id));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @RolesAllowed("STUDENT")
    @PostMapping("/{id}/enroll")
    ResponseEntity<?> enroll(@PathVariable Long id, Principal principal) {
        try {
            return ResponseEntity.ok(courseService.enroll(id, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/users")
    ResponseEntity<?> allUsersForCourse(@PathVariable Long id, @RequestParam(required = false) RoleType roleType) {
        try {
            return ResponseEntity.ok(courseService.getAllUserToCourseInfoForCourse(id, roleType));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @RolesAllowed("ADMIN")
    @PutMapping("/{id}/users/instructors")
    ResponseEntity<?> assignInstructor(@PathVariable Long id, @RequestParam Long instructorId) {
        try {
            return ResponseEntity.ok(courseService.assignInstructor(id, instructorId));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    //TODO the same as UserCourseController#getCoursesLessons
    @GetMapping("/{id}/users/{userId}")
    ResponseEntity<?> getUserToCourseInfo(@PathVariable Long id, @PathVariable Long userId) {
        try {
            return ResponseEntity.ok(courseService.getUsersCourseInfo(id, userId));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    //TODO think if we need to move it to UserCourseController
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}/users/{userId}")
    ResponseEntity<?> updateUsersCourseInfo(@PathVariable Long id,
                                            @PathVariable Long userId,
                                            @RequestBody UserToCourseDtoUpdate userToCourseDto) {
        try {
            return ResponseEntity.ok(courseService.updateUserToCourse(id, userId, userToCourseDto));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/lessons/{lessonId}/users/{userId}")
    ResponseEntity<?> getStudentLessonInfo(@PathVariable Long id, @PathVariable Long lessonId,
                                           @PathVariable Long userId) {
        try {
            if (!lessonService.hasWithCourse(lessonId, id)) {
                throw new InvalidPathException(String.format("Lesson %d in course %d doesn't exist", lessonId, id));
            }
            return ResponseEntity.ok(lessonService.getStudentLessonInfo(lessonId, userId));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}/lessons/{lessonId}/users/{userId}")
    ResponseEntity<?> updateUsersLessonInfo(@PathVariable Long id,
                                            @PathVariable Long lessonId,
                                            @PathVariable Long userId,
                                            @RequestBody UserToCourseDtoUpdate userToCourseDto) {
        try {
            if (!lessonService.hasWithCourse(lessonId, id)) {
                throw new InvalidPathException(String.format("Lesson %d in course %d doesn't exist", lessonId, id));
            }
            return ResponseEntity.ok(lessonService.updateUserToLesson(lessonId, userId, userToCourseDto));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

}
