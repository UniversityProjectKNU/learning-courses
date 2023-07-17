package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.dto.chapter.ChapterDtoSave;
import com.nazar.grynko.learningcourses.dto.chapter.ChapterDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("learning-courses/api/v1/courses/{courseId}/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    @Autowired
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping("/{id}")
    ChapterDto one(@PathVariable Long id, @PathVariable Long courseId) {
        if (!chapterService.hasWithCourse(id, courseId)) {
            throw new InvalidPathException();
        }

        return chapterService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<ChapterDto> allInCourse(@PathVariable Long courseId) {
        return chapterService.getAllInCourse(courseId);
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long courseId) {
        if (!chapterService.hasWithCourse(id, courseId)) {
            throw new InvalidPathException();
        }

        chapterService.delete(id);
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PostMapping
    ChapterDto save(@RequestBody ChapterDtoSave chapterDto, @PathVariable Long courseId) {
        return chapterService.save(chapterDto, courseId);
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}")
    ChapterDto update(@RequestBody ChapterDtoUpdate chapterDto, @PathVariable Long id, @PathVariable Long courseId) {
        if (!chapterService.hasWithCourse(id, courseId) || !Objects.equals(chapterDto.getId(), id)) {
            throw new InvalidPathException();
        }

        return chapterService.update(chapterDto, id);
    }

}
