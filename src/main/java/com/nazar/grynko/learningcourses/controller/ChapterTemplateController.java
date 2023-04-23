package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("learning-courses/api/v1/courses-templates/{courseTemplateId}/chapters-templates/")
public class ChapterTemplateController {

    private final ChapterTemplateService chapterTemplateService;

    public ChapterTemplateController(ChapterTemplateService chapterTemplateService) {
        this.chapterTemplateService = chapterTemplateService;
    }

    @GetMapping("/{id}")
    ChapterTemplateDto one(@PathVariable Long id, @PathVariable Long courseTemplateId) {
        if (!chapterTemplateService.hasWithCourseTemplate(id, courseTemplateId)) {
            throw new InvalidPathException();
        }

        return chapterTemplateService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<ChapterTemplateDto> allInCourse(@PathVariable Long courseTemplateId) {
        return chapterTemplateService.getAllInCourseTemplate(courseTemplateId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long courseTemplateId) {
        if (!chapterTemplateService.hasWithCourseTemplate(id, courseTemplateId)) {
            throw new InvalidPathException();
        }

        chapterTemplateService.delete(id);
    }

    @PostMapping
    ChapterTemplateDto save(@RequestBody ChapterTemplateDtoSave chapterTemplateDtoSave, @PathVariable Long courseTemplateId) {
        return chapterTemplateService.save(chapterTemplateDtoSave, courseTemplateId);
    }

    @PutMapping("/{id}")
    ChapterTemplateDto update(@RequestBody ChapterTemplateDtoUpdate chapterTemplateDto,
                              @PathVariable Long id, @PathVariable Long courseTemplateId) {
        if (!chapterTemplateDto.getId().equals(id) || !chapterTemplateService.hasWithCourseTemplate(id, courseTemplateId)) {
            throw new IllegalArgumentException();
        }

        return chapterTemplateService.update(chapterTemplateDto);
    }

}
