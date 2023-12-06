package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("learning-courses/api/v1/templates/chapters")
@RolesAllowed({"INSTRUCTOR", "ADMIN"})
public class ChapterTemplateController {

    private final ChapterTemplateService chapterTemplateService;
    private final LessonTemplateService lessonTemplateService;

    public ChapterTemplateController(ChapterTemplateService chapterTemplateService,
                                     LessonTemplateService lessonTemplateService) {
        this.chapterTemplateService = chapterTemplateService;
        this.lessonTemplateService = lessonTemplateService;
    }

    @GetMapping("/chapter")
    ResponseEntity<ChapterTemplateDto> one(@RequestParam Long chapterTemplateId) {
        return ResponseEntity.ok(chapterTemplateService.get(chapterTemplateId)
                .orElseThrow(InvalidPathException::new));
    }

    @GetMapping("/chapter/lessons")
    ResponseEntity<List<LessonTemplateDto>> allLessonsInChapterTemplate(@RequestParam Long chapterTemplateId) {
        return ResponseEntity.ok(lessonTemplateService.getAllInChapterTemplate(chapterTemplateId));
    }

    @DeleteMapping("/chapter")
    void delete(@RequestParam Long chapterTemplateId) {
        chapterTemplateService.delete(chapterTemplateId);
    }

    @PostMapping
    ResponseEntity<ChapterTemplateDto> save(@RequestParam Long courseTemplateId,
                                            @Valid @RequestBody ChapterTemplateDtoSave chapterTemplateDtoSave) {
        return ResponseEntity.ok(chapterTemplateService.save(chapterTemplateDtoSave, courseTemplateId));
    }

    @PutMapping("/chapter")
    ResponseEntity<ChapterTemplateDto> update(@RequestParam Long chapterTemplateId,
                                              @Valid @RequestBody ChapterTemplateDtoUpdate chapterTemplateDto) {
        return ResponseEntity.ok(chapterTemplateService.update(chapterTemplateDto, chapterTemplateId));
    }

}
