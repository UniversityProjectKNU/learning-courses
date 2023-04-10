package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses/{courseId}/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    @Autowired
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping("/{id}")
    ChapterDto one(@PathVariable Long id, @PathVariable Long courseId) {
        if(!chapterService.hasWithCourse(id, courseId))
            throw new InvalidPathException();

        return chapterService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<ChapterDto> allInCourse(@PathVariable Long courseId) {
        return chapterService.getAllInCourse(courseId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long courseId) {
        if(!chapterService.hasWithCourse(id, courseId))
            throw new InvalidPathException();

        chapterService.delete(id);
    }

    @PostMapping
    ChapterDto save(@RequestBody ChapterDto chapterDto, @PathVariable Long courseId) {
        return chapterService.save(chapterDto, courseId);
    }

    @PutMapping("/{id}")
    ChapterDto update(@RequestBody ChapterDto chapterDto, @PathVariable Long id, @PathVariable Long courseId) {
        if(!chapterService.hasWithCourse(id, courseId))
            throw new InvalidPathException();

        return chapterService.update(chapterDto, id);
    }

}
