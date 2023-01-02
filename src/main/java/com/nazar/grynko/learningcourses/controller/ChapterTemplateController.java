package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import com.nazar.grynko.learningcourses.service.CourseTemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("courses-templates/{courseTemplateId}/chapters-templates/")
public class ChapterTemplateController {

    private final ChapterTemplateService chapterTemplateService;
    private final CourseTemplateService courseTemplateService;

    public ChapterTemplateController(ChapterTemplateService chapterTemplateService, CourseTemplateService courseTemplateService) {
        this.chapterTemplateService = chapterTemplateService;
        this.courseTemplateService = courseTemplateService;
    }

    @GetMapping("/{id}")
    ChapterTemplate one(@PathVariable Long id) {
        return chapterTemplateService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    Set<ChapterTemplate> allInCourse(@PathVariable Long courseTemplateId) {
        CourseTemplate courseTemplate = courseTemplateService.get(courseTemplateId)
                .orElseThrow(InvalidPathException::new);

        return courseTemplate.getChapterTemplates();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        chapterTemplateService.delete(id);
    }

    @PostMapping
    ChapterTemplate save(@RequestBody ChapterTemplate lessonTemplate) {
        return chapterTemplateService.save(lessonTemplate);
    }

    @PutMapping("/{id}")
    void update(@RequestBody ChapterTemplate lessonTemplate) {
        chapterTemplateService.update(lessonTemplate);
    }

}
