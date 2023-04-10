package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateSave;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses-templates/{courseTemplateId}/chapters-templates/{chapterTemplateId}/lessons-templates")
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
        if(!lessonTemplateService.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId))
            throw new InvalidPathException();

        return lessonTemplateService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<LessonTemplateDto> allInChapter(@PathVariable Long chapterTemplateId, @PathVariable Long courseTemplateId) {
        if(!chapterTemplateService.hasWithCourseTemplate(chapterTemplateId, courseTemplateId))
            throw new InvalidPathException();

        return lessonTemplateService.getAllInChapterTemplate(chapterTemplateId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long chapterTemplateId, @PathVariable Long courseTemplateId) {
        if(!lessonTemplateService.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId))
            throw new InvalidPathException();

        lessonTemplateService.delete(id);
    }

    @PostMapping
    LessonTemplateDto save(@RequestBody LessonTemplateSave lessonTemplateSave, @PathVariable Long chapterTemplateId,
                           @PathVariable Long courseTemplateId) {
        if(!chapterTemplateService.hasWithCourseTemplate(chapterTemplateId, courseTemplateId))
            throw new InvalidPathException();

        return lessonTemplateService.save(lessonTemplateSave, chapterTemplateId);
    }

    @PutMapping("/{id}")
    LessonTemplateDto update(@RequestBody LessonTemplateDto lessonTemplateDto, @PathVariable Long id,
                             @PathVariable Long chapterTemplateId, @PathVariable Long courseTemplateId) {
        if(!lessonTemplateDto.getId().equals(id))
            throw new InvalidPathException();
        if(!lessonTemplateService.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId))
            throw new InvalidPathException();

        return lessonTemplateService.update(lessonTemplateDto);
    }

}
