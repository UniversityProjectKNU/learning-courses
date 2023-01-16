package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.ChapterServiceWrapper;
import com.nazar.grynko.learningcourses.wrapper.ChapterTemplateServiceWrapper;
import com.nazar.grynko.learningcourses.wrapper.LessonServiceWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses/{courseId}/chapters/{chapterId}/lessons")
public class LessonController {

    private final LessonServiceWrapper serviceWrapper;
    private final ChapterServiceWrapper chapterServiceWrapper;

    public LessonController(LessonServiceWrapper serviceWrapper, ChapterServiceWrapper chapterServiceWrapper) {
        this.serviceWrapper = serviceWrapper;
        this.chapterServiceWrapper = chapterServiceWrapper;
    }

    @GetMapping("/{id}")
    LessonDto one(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if(!serviceWrapper.hasWithChapter(id, chapterId, courseId))
            throw new InvalidPathException();


        return serviceWrapper.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<LessonDto> allInChapter(@PathVariable Long chapterId, @PathVariable Long courseId) {
        if(!chapterServiceWrapper.hasWithCourse(chapterId, courseId))
            throw new InvalidPathException();

        return serviceWrapper.getAllInChapter(chapterId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if(!serviceWrapper.hasWithChapter(id, chapterId, courseId))
            throw new InvalidPathException();

        serviceWrapper.delete(id);
    }

    @PostMapping
    LessonDto save(@RequestBody LessonDto lessonDto, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if(!chapterServiceWrapper.hasWithCourse(chapterId, courseId))
            throw new InvalidPathException();

        return serviceWrapper.save(lessonDto, chapterId);
    }

    @PutMapping("/{id}")
    LessonDto update(@RequestBody LessonDto lessonDto, @PathVariable Long id,
                     @PathVariable Long chapterId, @PathVariable Long courseId) {
        if(!serviceWrapper.hasWithChapter(id, chapterId, courseId))
            throw new InvalidPathException();

        return serviceWrapper.update(lessonDto, id);
    }

}
