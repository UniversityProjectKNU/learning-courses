package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.ChapterTemplateServiceWrapper;
import com.nazar.grynko.learningcourses.wrapper.LessonTemplateServiceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("courses-templates/{courseTemplateId}/chapters-templates/{chapterTemplateId}/lessons-templates")
public class LessonTemplateController {

    private final LessonTemplateServiceWrapper serviceWrapper;
    private final ChapterTemplateServiceWrapper chapterTemplateServiceWrapper;

    @Autowired
    public LessonTemplateController(LessonTemplateServiceWrapper serviceWrapper, ChapterTemplateServiceWrapper chapterTemplateServiceWrapper) {
        this.serviceWrapper = serviceWrapper;
        this.chapterTemplateServiceWrapper = chapterTemplateServiceWrapper;
    }


    @GetMapping("/{id}")
    LessonTemplateDto one(@PathVariable Long id, @PathVariable Long chapterTemplateId,
                          @PathVariable Long courseTemplateId) {
        if(!serviceWrapper.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId))
            throw new InvalidPathException();


        return serviceWrapper.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    Set<LessonTemplateDto> allInChapter(@PathVariable Long chapterTemplateId, @PathVariable Long courseTemplateId) {
        if(!chapterTemplateServiceWrapper.hasWithCourseTemplate(chapterTemplateId, courseTemplateId))
            throw new InvalidPathException();

        return serviceWrapper.getAllInChapterTemplate(chapterTemplateId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long chapterTemplateId, @PathVariable Long courseTemplateId) {
        if(!serviceWrapper.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId))
            throw new InvalidPathException();

        serviceWrapper.delete(id);
    }

    @PostMapping
    LessonTemplateDto save(@RequestBody LessonTemplateDto lessonTemplateDto, @PathVariable Long chapterTemplateId,
                           @PathVariable Long courseTemplateId) {
        if(!chapterTemplateServiceWrapper.hasWithCourseTemplate(chapterTemplateId, courseTemplateId))
            throw new InvalidPathException();

        return serviceWrapper.save(lessonTemplateDto, chapterTemplateId);
    }

    @PutMapping("/{id}")
    LessonTemplateDto update(@RequestBody LessonTemplateDto lessonTemplateDto, @PathVariable Long id,
                             @PathVariable Long chapterTemplateId, @PathVariable Long courseTemplateId) {
        if(!serviceWrapper.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId))
            throw new InvalidPathException();

        return serviceWrapper.update(lessonTemplateDto, id);
    }

}
