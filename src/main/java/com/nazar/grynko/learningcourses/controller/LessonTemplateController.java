package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._200;
import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._404;

@RestController
@RequestMapping("learning-courses/api/v1/templates/lessons")
@RolesAllowed({"INSTRUCTOR", "ADMIN"})
public class LessonTemplateController {

    private final LessonTemplateService lessonTemplateService;

    @Autowired
    public LessonTemplateController(LessonTemplateService lessonTemplateService) {
        this.lessonTemplateService = lessonTemplateService;
    }

    @Operation(summary = "Get a lesson template",
            description = "Get a lesson template by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the lesson template"),
            @ApiResponse(responseCode = _404, description = "Lesson template not found")
    })
    @GetMapping("/lesson")
    ResponseEntity<LessonTemplateDto> one(@RequestParam Long lessonTemplateId) {
        return ResponseEntity.ok(lessonTemplateService.get(lessonTemplateId));
    }

    @Operation(summary = "Delete a lesson template",
            description = "Delete a lesson template by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Lesson template deleted"),
            @ApiResponse(responseCode = _404, description = "Lesson template not found")
    })
    @DeleteMapping("/lesson")
    void delete(@RequestParam Long lessonTemplateId) {
        lessonTemplateService.delete(lessonTemplateId);
    }

    @Operation(summary = "Save a lesson template",
            description = "Save an empty lesson template. This lesson template is applied to course template with courseTemplateId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Lesson template saved"),
            @ApiResponse(responseCode = _404, description = "Parent course template not found")
    })
    @PostMapping
    ResponseEntity<LessonTemplateDto> save(@RequestParam Long chapterTemplateId,
                                           @Valid @RequestBody LessonTemplateDtoSave lessonTemplateDtoSave) {
        return ResponseEntity.ok(lessonTemplateService.save(lessonTemplateDtoSave, chapterTemplateId));
    }

    @Operation(summary = "Update a lesson template",
            description = "Update lesson template's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Lesson template updated"),
            @ApiResponse(responseCode = _404, description = "Lesson template not found")
    })
    @PutMapping("/lesson")
    ResponseEntity<LessonTemplateDto> update(@RequestParam Long lessonTemplateId,
                                             @Valid @RequestBody LessonTemplateDtoUpdate lessonTemplateDto) {
        return ResponseEntity.ok(lessonTemplateService.update(lessonTemplateDto, lessonTemplateId));
    }

}
