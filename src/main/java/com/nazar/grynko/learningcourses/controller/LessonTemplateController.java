package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
    LessonTemplate one(@PathVariable Long id) {
        return lessonTemplateService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    Set<LessonTemplate> allInChapter(@PathVariable Long chapterTemplateId) {
        ChapterTemplate chapterTemplate = chapterTemplateService.get(chapterTemplateId)
                .orElseThrow(InvalidPathException::new);

        return chapterTemplate.getLessonTemplates();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        lessonTemplateService.delete(id);
    }

    @PostMapping
    LessonTemplate save(@RequestBody LessonTemplate lessonTemplate) {
        return lessonTemplateService.save(lessonTemplate);
    }

    @PutMapping("/{id}")
    void update(@RequestBody LessonTemplate lessonTemplate) {
        lessonTemplateService.update(lessonTemplate);
    }

}
