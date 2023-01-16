package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.ChapterServiceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses/{courseId}/chapters")
public class ChapterController {

    private final ChapterServiceWrapper serviceWrapper;

    @Autowired
    public ChapterController(ChapterServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping("/{id}")
    ChapterDto one(@PathVariable Long id, @PathVariable Long courseId) {
        if(!serviceWrapper.hasWithCourse(id, courseId))
            throw new InvalidPathException();

        return serviceWrapper.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<ChapterDto> allInCourse(@PathVariable Long courseId) {
        return serviceWrapper.getAllInCourse(courseId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long courseId) {
        if(!serviceWrapper.hasWithCourse(id, courseId))
            throw new InvalidPathException();

        serviceWrapper.delete(id);
    }

    @PostMapping
    ChapterDto save(@RequestBody ChapterDto chapterDto, @PathVariable Long courseId) {
        return serviceWrapper.save(chapterDto, courseId);
    }

    @PutMapping("/{id}")
    ChapterDto update(@RequestBody ChapterDto chapterDto, @PathVariable Long id, @PathVariable Long courseId) {
        if(!serviceWrapper.hasWithCourse(id, courseId))
            throw new InvalidPathException();

        return serviceWrapper.update(chapterDto, id);
    }

}
