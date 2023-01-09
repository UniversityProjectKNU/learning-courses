package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.ChapterTemplateServiceWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("courses-templates/{courseTemplateId}/chapters-templates/")
public class ChapterTemplateController {

    private final ChapterTemplateServiceWrapper serviceWrapper;

    public ChapterTemplateController(ChapterTemplateServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping("/{id}")
    ChapterTemplateDto one(@PathVariable Long id) {
        return serviceWrapper.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    Set<ChapterTemplateDto> allInCourse(@PathVariable Long courseTemplateId) {
        return serviceWrapper.getAllInCourseTemplate(courseTemplateId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        serviceWrapper.delete(id);
    }

    @PostMapping
    ChapterTemplateDto save(@RequestBody ChapterTemplateDto chapterTemplateDto, @PathVariable Long courseTemplateId) {
        return serviceWrapper.save(chapterTemplateDto, courseTemplateId);
    }

    @PutMapping("/{id}")
    ChapterTemplateDto update(@RequestBody ChapterTemplateDto chapterTemplateDto, @PathVariable Long id) {
        return serviceWrapper.update(chapterTemplateDto, id);
    }

}
