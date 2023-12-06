package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("learning-courses/api/v1/templates/lessons")
@RolesAllowed({"INSTRUCTOR", "ADMIN"})
public class LessonTemplateController {

    private final LessonTemplateService lessonTemplateService;

    @Autowired
    public LessonTemplateController(LessonTemplateService lessonTemplateService) {
        this.lessonTemplateService = lessonTemplateService;
    }

    @GetMapping("/lesson")
    ResponseEntity<LessonTemplateDto> one(@RequestParam Long lessonTemplateId) {
        return ResponseEntity.ok(lessonTemplateService.get(lessonTemplateId)
                .orElseThrow(InvalidPathException::new));
    }

    @DeleteMapping("/lesson")
    void delete(@RequestParam Long lessonTemplateId) {
        lessonTemplateService.delete(lessonTemplateId);
    }

    @PostMapping
    ResponseEntity<LessonTemplateDto> save(@RequestParam Long chapterTemplateId,
                                           @Valid @RequestBody LessonTemplateDtoSave lessonTemplateDtoSave) {
        return ResponseEntity.ok(lessonTemplateService.save(lessonTemplateDtoSave, chapterTemplateId));
    }

    @PutMapping("/lesson")
    ResponseEntity<LessonTemplateDto> update(@RequestParam Long lessonTemplateId,
                                             @Valid @RequestBody LessonTemplateDtoUpdate lessonTemplateDto) {
        return ResponseEntity.ok(lessonTemplateService.update(lessonTemplateDto, lessonTemplateId));
    }

}
