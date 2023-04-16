package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoSave;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterService;
import com.nazar.grynko.learningcourses.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("learning-courses/api/v1/courses/{courseId}/chapters/{chapterId}/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final ChapterService chapterService;

    public LessonController(LessonService lessonService, ChapterService chapterService) {
        this.lessonService = lessonService;
        this.chapterService = chapterService;
    }

    @GetMapping("/{id}")
    LessonDto one(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId))
            throw new InvalidPathException();


        return lessonService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<LessonDto> allInChapter(@PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!chapterService.hasWithCourse(chapterId, courseId))
            throw new InvalidPathException();

        return lessonService.getAllInChapter(chapterId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId))
            throw new InvalidPathException();

        lessonService.delete(id);
    }

    @PostMapping
    LessonDto save(@RequestBody LessonDtoSave lessonDto, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!chapterService.hasWithCourse(chapterId, courseId))
            throw new InvalidPathException();

        return lessonService.save(lessonDto, chapterId);
    }

    @PutMapping("/{id}")
    LessonDto update(@RequestBody LessonDto lessonDto, @PathVariable Long id,
                     @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId) || !Objects.equals(lessonDto.getId(), id))
            throw new InvalidPathException();

        return lessonService.update(lessonDto, id);
    }

}
