package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoSave;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoUpdate;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterService;
import com.nazar.grynko.learningcourses.service.LessonService;
import com.nazar.grynko.learningcourses.service.UserToLessonService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("learning-courses/api/v1/courses/{courseId}/chapters/{chapterId}/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final ChapterService chapterService;
    private final UserToLessonService userToLessonService;

    public LessonController(LessonService lessonService, ChapterService chapterService,
                            UserToLessonService userToLessonService) {
        this.lessonService = lessonService;
        this.chapterService = chapterService;
        this.userToLessonService = userToLessonService;
    }

    @GetMapping("/{id}")
    LessonDto one(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }


        return lessonService.get(id)
                .orElseThrow(InvalidPathException::new);
    }

    @GetMapping
    List<LessonDto> allInChapter(@PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!chapterService.hasWithCourse(chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        return lessonService.getAllInChapter(chapterId);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        lessonService.delete(id);
    }

    @PostMapping
    LessonDto save(@RequestBody LessonDtoSave lessonDto, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!chapterService.hasWithCourse(chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        return lessonService.save(lessonDto, chapterId);
    }

    @PutMapping("/{id}")
    LessonDto update(@RequestBody LessonDtoUpdate lessonDto, @PathVariable Long id,
                     @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId) || !Objects.equals(lessonDto.getId(), id)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        return lessonService.update(lessonDto, id);
    }

    @PostMapping("/{id}/files/upload")
    public ResponseEntity<?> upload(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId,
                                    @RequestBody MultipartFile file, Principal principal) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        try {
            return ResponseEntity.ok(userToLessonService.upload(id, principal.getName(), file));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/files/download")
    public ResponseEntity<ByteArrayResource> download(@PathVariable Long id, @PathVariable Long chapterId,
                                                      @PathVariable Long courseId, Principal principal) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        var fileDto = userToLessonService.download(id, principal.getName());
        var data = fileDto.getData();

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileDto.getTitle() + "\"")
                .body(new ByteArrayResource(data));
    }

    @DeleteMapping("/{id}/files/delete")
    public void delete(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId, Principal principal) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        userToLessonService.delete(id, principal.getName());
    }

}
