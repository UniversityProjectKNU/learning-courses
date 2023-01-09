package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.LessonTemplateServiceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("courses-templates/{courseTemplateId}/chapters-templates/{chapterTemplateId}/lessons-templates")
public class LessonTemplateController {

    private final LessonTemplateServiceWrapper serviceWrapper;

    @Autowired
    public LessonTemplateController(LessonTemplateServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }


    @GetMapping("/{id}")
    LessonTemplateDto one(@PathVariable Long id) {
        return serviceWrapper.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    Set<LessonTemplateDto> allInChapter(@PathVariable Long chapterTemplateId) {
        return serviceWrapper.getAllInChapterTemplate(chapterTemplateId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        serviceWrapper.delete(id);
    }

    @PostMapping
    LessonTemplateDto save(@RequestBody LessonTemplateDto lessonTemplateDto, @PathVariable Long chapterTemplateId) {
        return serviceWrapper.save(lessonTemplateDto, chapterTemplateId);
    }

    @PutMapping("/{id}")
    LessonTemplateDto update(@RequestBody LessonTemplateDto lessonTemplateDto, @PathVariable Long id) {
        return serviceWrapper.update(lessonTemplateDto, id);
    }

}
