package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoSave;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        courseService.delete(id);
    }

    @PostMapping
    CourseDto save(@RequestBody CourseDtoSave courseDto) {
        return courseService.save(courseDto);
    }

    @PutMapping("/{id}")
    CourseDto update(@RequestBody CourseDtoUpdate courseDto, @PathVariable Long id) {
        if (!Objects.equals(courseDto.getId(), id)) {
            throw new InvalidPathException();
        }

        return courseService.update(courseDto, id);
    }

    @PutMapping("/{id}/chapters/")
    ResponseEntity<?> getAllChapters(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(courseService.getAllChapters(id));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/lessons/")
    ResponseEntity<?> getAllLessons(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(courseService.getAllLessons(id));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

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
            return ResponseEntity.ok(courseService.getAllInstructorsForCourse(id, roleType));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

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

}
