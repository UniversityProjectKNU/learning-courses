package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._200;
import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._404;

@RestController
@RequestMapping("learning-courses/api/v1/templates/chapters")
@RolesAllowed({"INSTRUCTOR", "ADMIN"})
public class ChapterTemplateController {

    private final ChapterTemplateService chapterTemplateService;
    private final LessonTemplateService lessonTemplateService;

    public ChapterTemplateController(ChapterTemplateService chapterTemplateService,
                                     LessonTemplateService lessonTemplateService) {
        this.chapterTemplateService = chapterTemplateService;
        this.lessonTemplateService = lessonTemplateService;
    }

    @Operation(summary = "Get a chapter template",
            description = "Get a chapter template by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the chapter template"),
            @ApiResponse(responseCode = _404, description = "Chapter template not found")
    })
    @GetMapping("/chapter")
    ResponseEntity<ChapterTemplateDto> one(@RequestParam Long chapterTemplateId) {
        return ResponseEntity.ok(chapterTemplateService.get(chapterTemplateId));
    }

    @Operation(summary = "Get chapter template's lessons",
            description = "Get a list of lesson templates for a specific chapter template. List of lesson templates is retrieved by the chapter template id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all the lesson templates of the chapter template"),
            @ApiResponse(responseCode = _404, description = "Parent chapter template not found")
    })
    @GetMapping("/chapter/lessons")
    ResponseEntity<List<LessonTemplateDto>> allLessonsInChapterTemplate(@RequestParam Long chapterTemplateId) {
        return ResponseEntity.ok(lessonTemplateService.getAllInChapterTemplate(chapterTemplateId));
    }

    @Operation(summary = "Delete a chapter template",
            description = "Delete a chapter template by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Chapter template deleted"),
            @ApiResponse(responseCode = _404, description = "Chapter template not found")
    })
    @DeleteMapping("/chapter")
    void delete(@RequestParam Long chapterTemplateId) {
        chapterTemplateService.delete(chapterTemplateId);
    }

    @Operation(summary = "Save a chapter template",
            description = "Save an empty chapter template. This chapter template is applied to course template with courseTemplateId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Chapter template saved"),
            @ApiResponse(responseCode = _404, description = "Parent course template not found")
    })
    @PostMapping
    ResponseEntity<ChapterTemplateDto> save(@RequestParam Long courseTemplateId,
                                            @Valid @RequestBody ChapterTemplateDtoSave chapterTemplateDtoSave) {
        return ResponseEntity.ok(chapterTemplateService.save(chapterTemplateDtoSave, courseTemplateId));
    }

    @Operation(summary = "Update a chapter template",
            description = "Update chapter template's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Chapter template updated"),
            @ApiResponse(responseCode = _404, description = "Chapter template not found")
    })
    @PutMapping("/chapter")
    ResponseEntity<ChapterTemplateDto> update(@RequestParam Long chapterTemplateId,
                                              @Valid @RequestBody ChapterTemplateDtoUpdate chapterTemplateDto) {
        return ResponseEntity.ok(chapterTemplateService.update(chapterTemplateDto, chapterTemplateId));
    }

}
