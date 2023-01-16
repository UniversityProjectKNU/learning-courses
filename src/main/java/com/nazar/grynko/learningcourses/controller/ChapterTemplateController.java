package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateSave;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.ChapterTemplateServiceWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses-templates/{courseTemplateId}/chapters-templates/")
public class ChapterTemplateController {

    private final ChapterTemplateServiceWrapper serviceWrapper;

    public ChapterTemplateController(ChapterTemplateServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping("/{id}")
    ChapterTemplateDto one(@PathVariable Long id, @PathVariable Long courseTemplateId) {
        if(!serviceWrapper.hasWithCourseTemplate(id, courseTemplateId))
            throw new InvalidPathException();

        return serviceWrapper.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<ChapterTemplateDto> allInCourse(@PathVariable Long courseTemplateId) {
        return serviceWrapper.getAllInCourseTemplate(courseTemplateId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long courseTemplateId) {
        if(!serviceWrapper.hasWithCourseTemplate(id, courseTemplateId))
            throw new InvalidPathException();

        serviceWrapper.delete(id);
    }

    @PostMapping
    ChapterTemplateDto save(@RequestBody ChapterTemplateSave chapterTemplateSave, @PathVariable Long courseTemplateId) {
        return serviceWrapper.save(chapterTemplateSave, courseTemplateId);
    }

    @PutMapping("/{id}")
    ChapterTemplateDto update(@RequestBody ChapterTemplateDto chapterTemplateDto,
                              @PathVariable Long id, @PathVariable Long courseTemplateId) {
        if(!chapterTemplateDto.getId().equals(id))
            throw new IllegalArgumentException();
        if(!serviceWrapper.hasWithCourseTemplate(id, courseTemplateId))
            throw new InvalidPathException();

        return serviceWrapper.update(chapterTemplateDto);
    }

}
