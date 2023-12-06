package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.dto.chapter.ChapterDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterService;
import com.nazar.grynko.learningcourses.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("learning-courses/api/v1/chapters")
public class ChapterController {

    private final ChapterService chapterService;
    private final LessonService lessonService;

    @Autowired
    public ChapterController(ChapterService chapterService,
                             LessonService lessonService) {
        this.chapterService = chapterService;
        this.lessonService = lessonService;
    }

    @GetMapping("/chapter")
    ResponseEntity<ChapterDto> one(@RequestParam Long chapterId) {
        return ResponseEntity.ok(chapterService.get(chapterId)
                .orElseThrow(InvalidPathException::new));
    }

    @GetMapping("/chapter/lessons")
    ResponseEntity<List<LessonDto>> allLessonsInChapter(@RequestParam Long chapterId) {
        return ResponseEntity.ok(lessonService.getAllInChapter(chapterId));
    }

    @GetMapping("/chapter/users")
    ResponseEntity<List<UserToLessonDto>> allUsersToLessonsInChapter(@RequestParam Long chapterId) {
        return ResponseEntity.ok(lessonService.getAllUserToLessonInChapter(chapterId));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @DeleteMapping("/chapter")
    void delete(@RequestParam Long chapterId) {
        chapterService.delete(chapterId);
    }

/*    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PostMapping
    ResponseEntity<ChapterDto> save(@RequestParam Long courseId, @RequestBody ChapterDtoSave chapterDto) {
        return ResponseEntity.ok(chapterService.save(chapterDto, courseId));
    }*/

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/chapter")
    ResponseEntity<ChapterDto> update(@RequestParam Long chapterId,
                                      @Valid @RequestBody ChapterDtoUpdate chapterDto) {
        return ResponseEntity.ok(chapterService.update(chapterDto, chapterId));
    }

}
