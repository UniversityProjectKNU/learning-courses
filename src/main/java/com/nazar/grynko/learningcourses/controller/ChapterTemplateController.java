package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("learning-courses/api/v1/courses-templates/{courseTemplateId}/chapters-templates/")
@RolesAllowed({"INSTRUCTOR", "ADMIN"})
public class ChapterTemplateController {

    private final ChapterTemplateService chapterTemplateService;

    public ChapterTemplateController(ChapterTemplateService chapterTemplateService) {
        this.chapterTemplateService = chapterTemplateService;
    }

    @GetMapping("/{id}")
    ResponseEntity<ChapterTemplateDto> one(@PathVariable Long id, @PathVariable Long courseTemplateId) {
        if (!chapterTemplateService.hasWithCourseTemplate(id, courseTemplateId)) {
            throw new InvalidPathException();
        }

        return ResponseEntity.ok(chapterTemplateService.get(id)
                .orElseThrow(InvalidPathException::new));
    }

    @GetMapping
    ResponseEntity<List<ChapterTemplateDto>> allInCourse(@PathVariable Long courseTemplateId) {
        return ResponseEntity.ok(chapterTemplateService.getAllInCourseTemplate(courseTemplateId));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long courseTemplateId) {
        if (!chapterTemplateService.hasWithCourseTemplate(id, courseTemplateId)) {
            throw new InvalidPathException();
        }

        chapterTemplateService.delete(id);
    }

    @PostMapping
    ResponseEntity<ChapterTemplateDto> save(@RequestBody ChapterTemplateDtoSave chapterTemplateDtoSave, @PathVariable Long courseTemplateId) {
        return ResponseEntity.ok(chapterTemplateService.save(chapterTemplateDtoSave, courseTemplateId));
    }

    @PutMapping("/{id}")
    ResponseEntity<ChapterTemplateDto> update(@RequestBody ChapterTemplateDtoUpdate chapterTemplateDto,
                              @PathVariable Long id, @PathVariable Long courseTemplateId) {
        if (!chapterTemplateDto.getId().equals(id) || !chapterTemplateService.hasWithCourseTemplate(id, courseTemplateId)) {
            throw new IllegalArgumentException();
        }

        return ResponseEntity.ok(chapterTemplateService.update(chapterTemplateDto));
    }

}
