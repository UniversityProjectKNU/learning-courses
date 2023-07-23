package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("learning-courses/api/v1/courses-templates/{courseTemplateId}/chapters-templates/{chapterTemplateId}/lessons-templates")
@RolesAllowed({"INSTRUCTOR", "ADMIN"})
public class LessonTemplateController {

    private final LessonTemplateService lessonTemplateService;
    private final ChapterTemplateService chapterTemplateService;

    @Autowired
    public LessonTemplateController(LessonTemplateService lessonTemplateService, ChapterTemplateService chapterTemplateService) {
        this.lessonTemplateService = lessonTemplateService;
        this.chapterTemplateService = chapterTemplateService;
    }

    @GetMapping("/{id}")
    LessonTemplateDto one(@PathVariable Long id, @PathVariable Long chapterTemplateId,
                          @PathVariable Long courseTemplateId) {
        if (!lessonTemplateService.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId)) {
            throw new InvalidPathException();
        }

        return lessonTemplateService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<LessonTemplateDto> allInChapter(@PathVariable Long chapterTemplateId, @PathVariable Long courseTemplateId) {
        if (!chapterTemplateService.hasWithCourseTemplate(chapterTemplateId, courseTemplateId)) {
            throw new InvalidPathException();
        }

        return lessonTemplateService.getAllInChapterTemplate(chapterTemplateId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long chapterTemplateId, @PathVariable Long courseTemplateId) {
        if (!lessonTemplateService.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId)) {
            throw new InvalidPathException();
        }

        lessonTemplateService.delete(id);
    }

    @PostMapping
    LessonTemplateDto save(@RequestBody LessonTemplateDtoSave lessonTemplateDtoSave, @PathVariable Long chapterTemplateId,
                           @PathVariable Long courseTemplateId) {
        if (!chapterTemplateService.hasWithCourseTemplate(chapterTemplateId, courseTemplateId)) {
            throw new InvalidPathException();
        }

        return lessonTemplateService.save(lessonTemplateDtoSave, chapterTemplateId);
    }

    @PutMapping("/{id}")
    LessonTemplateDto update(@RequestBody LessonTemplateDtoUpdate lessonTemplateDto, @PathVariable Long id,
                             @PathVariable Long chapterTemplateId, @PathVariable Long courseTemplateId) {
        if (!lessonTemplateDto.getId().equals(id) || !lessonTemplateService.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId)) {
            throw new InvalidPathException();
        }

        return lessonTemplateService.update(lessonTemplateDto);
    }

}
