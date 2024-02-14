package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.CourseTemplateService;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode.*;

@RestController
@RequestMapping("learning-courses/api/v1/templates/courses")
@RolesAllowed({"ADMIN", "INSTRUCTOR"})
public class CourseTemplateController {

    private final CourseTemplateService courseTemplateService;
    private final ChapterTemplateService chapterTemplateService;
    private final LessonTemplateService lessonTemplateService;
    private final CourseService courseService;

    public CourseTemplateController(CourseTemplateService courseTemplateService,
                                    ChapterTemplateService chapterTemplateService,
                                    LessonTemplateService lessonTemplateService,
                                    CourseService courseService) {
        this.courseTemplateService = courseTemplateService;
        this.chapterTemplateService = chapterTemplateService;
        this.lessonTemplateService = lessonTemplateService;
        this.courseService = courseService;
    }

    @Operation(summary = "Get a course template",
            description = "Get a course template by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the course template"),
            @ApiResponse(responseCode = _404, description = "Course template not found")
    })
    @GetMapping("/course")
    ResponseEntity<CourseTemplateDto> one(@RequestParam Long courseTemplateId) {
        return ResponseEntity.ok(courseTemplateService.get(courseTemplateId));
    }

    @Operation(summary = "Get all course templates",
            description = "Get a list of all course templates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all course templates")
    })
    @GetMapping
    List<CourseTemplateDto> all() {
        return courseTemplateService.getAll();
    }

    @Operation(summary = "Get course template's lessons",
            description = "Get a list of all lesson templates for a specific course template. List of lesson templates is retrieved by the course template id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all the lesson templates of the course template"),
            @ApiResponse(responseCode = _404, description = "Parent course template not found")
    })
    @GetMapping("/course/lessons")
    ResponseEntity<List<LessonTemplateDto>> allLessonsInCourseTemplate(@RequestParam Long courseTemplateId) {
        return ResponseEntity.ok(lessonTemplateService.getAllInCourseTemplate(courseTemplateId));
    }

    @Operation(summary = "Get course template's chapters",
            description = "Get a list of all chapter templates for a specific course template. List of chapter templates is retrieved by the course template id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all the chapter templates of the course template"),
            @ApiResponse(responseCode = _404, description = "Parent course template not found")
    })
    @GetMapping("/course/chapters")
    ResponseEntity<List<ChapterTemplateDto>> allChaptersInCourseTemplate(@RequestParam Long courseTemplateId) {
        return ResponseEntity.ok(chapterTemplateService.getAllInCourseTemplate(courseTemplateId));
    }

    @Operation(summary = "Delete a course template",
            description = "Delete a course template by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Course template deleted"),
            @ApiResponse(responseCode = _404, description = "Course template not found")
    })
    @DeleteMapping("/course")
    void delete(@RequestParam Long courseTemplateId) {
        courseTemplateService.delete(courseTemplateId);
    }

    @Operation(summary = "Save a course template",
            description = "Save an empty course template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Course template saved")
    })
    @PostMapping
    ResponseEntity<CourseTemplateDto> save(@Valid @RequestBody CourseTemplateDtoSave courseTemplateDtoSave) {
        return ResponseEntity.ok(courseTemplateService.save(courseTemplateDtoSave));
    }

    @Operation(summary = "Update a course template",
            description = "Update course template's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Course template updated"),
            @ApiResponse(responseCode = _404, description = "Course template not found")
    })
    @PutMapping("/course")
    ResponseEntity<CourseTemplateDto> update(@RequestParam Long courseTemplateId,
                                             @Valid @RequestBody CourseTemplateDtoUpdate courseTemplateDto) {
        return ResponseEntity.ok(courseTemplateService.update(courseTemplateDto, courseTemplateId));
    }

    @Operation(summary = "Create an instance of the course",
            description = "Calling this method you create a new course with all its chapters and lessons, only if course template is valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "New course created"),
            @ApiResponse(responseCode = _400, description = "Course template is invalid")
    })
    @RolesAllowed("INSTRUCTOR")
    @PostMapping("/course/create-instance")
    ResponseEntity<CourseDto> createInstance(@RequestParam Long courseTemplateId, Principal principal) {
        return ResponseEntity.ok(courseService.create(courseTemplateId, principal.getName()));
    }

}
