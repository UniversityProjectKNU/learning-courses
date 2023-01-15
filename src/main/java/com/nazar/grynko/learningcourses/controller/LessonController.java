package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.wrapper.LessonServiceWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("courses/{courseId}/chapters/{chapterId}/lessons")
public class LessonController {

    private final LessonServiceWrapper serviceWrapper;

    public LessonController(LessonServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }


    @GetMapping("/{id}")
    LessonDto one(@PathVariable Long id) {
        return serviceWrapper.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<LessonDto> allInChapter(@PathVariable Long chapterId) {
        return serviceWrapper.getAllInChapter(chapterId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        serviceWrapper.delete(id);
    }

    @PostMapping
    LessonDto save(@RequestBody LessonDto lessonDto, @PathVariable Long chapterId) {
        return serviceWrapper.save(lessonDto, chapterId);
    }

    @PutMapping("/{id}")
    LessonDto update(@RequestBody LessonDto lessonDto, @PathVariable Long id) {
        return serviceWrapper.update(lessonDto, id);
    }

}
