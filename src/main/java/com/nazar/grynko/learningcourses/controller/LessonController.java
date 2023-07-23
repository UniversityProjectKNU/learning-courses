package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoSave;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoUpdate;
import com.nazar.grynko.learningcourses.dto.simple.SimpleDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.service.ChapterService;
import com.nazar.grynko.learningcourses.service.LessonService;
import com.nazar.grynko.learningcourses.service.UserToLessonService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
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
    ResponseEntity<LessonDto> one(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        return ResponseEntity.ok(lessonService.get(id)
                .orElseThrow(InvalidPathException::new));
    }

    @GetMapping
    ResponseEntity<List<LessonDto>> allInChapter(@PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!chapterService.hasWithCourse(chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        return ResponseEntity.ok(lessonService.getAllInChapter(chapterId));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        lessonService.delete(id);
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PostMapping
    ResponseEntity<LessonDto> save(@RequestBody LessonDtoSave lessonDto, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!chapterService.hasWithCourse(chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        return ResponseEntity.ok(lessonService.save(lessonDto, chapterId));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}")
    ResponseEntity<LessonDto> update(@RequestBody LessonDtoUpdate lessonDto, @PathVariable Long id,
                     @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId) || !Objects.equals(lessonDto.getId(), id)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        return ResponseEntity.ok(lessonService.update(lessonDto, id));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/{id}/finish")
    ResponseEntity<LessonDto> finishLesson(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }
        return ResponseEntity.ok(lessonService.finish(id));
    }

    //TODO when file's size is bigger then we can allow
    @PostMapping("/{id}/files/upload")
    public ResponseEntity<HomeworkFileDto> upload(@PathVariable Long id, @PathVariable Long chapterId, @PathVariable Long courseId,
                                                  @RequestBody MultipartFile file, Principal principal) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }
        else if (lessonService.isFinished(id)) {
            throw new InvalidPathException("You cannot upload file in finished lesson");
        }

        return ResponseEntity.ok(userToLessonService.upload(id, principal.getName(), file));
    }

    @GetMapping("/{id}/files/info")
    public ResponseEntity<HomeworkFileDto> getFileInfo(@PathVariable Long id, @PathVariable Long chapterId,
                                         @PathVariable Long courseId, Principal principal) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        return ResponseEntity.ok(userToLessonService.getFileInfo(id, principal.getName()));
    }

    @GetMapping("/{id}/files/info/{userId}")
    public ResponseEntity<HomeworkFileDto> getStudentFileInfo(@PathVariable Long id, @PathVariable Long chapterId,
                                         @PathVariable Long courseId, @PathVariable Long userId) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }

        return ResponseEntity.ok(userToLessonService.getFileInfo(id, userId));
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
    public ResponseEntity<SimpleDto<String>> delete(@PathVariable Long id, @PathVariable Long chapterId,
                                                    @PathVariable Long courseId, Principal principal) {
        if (!lessonService.hasWithChapter(id, chapterId, courseId)) {
            throw new InvalidPathException("Such lesson doesn't exist");
        }
        else if (lessonService.isFinished(id)) {
            throw new InvalidPathException("You cannot upload file in finished lesson");
        }

        userToLessonService.delete(id, principal.getName());
        return ResponseEntity.ok(new SimpleDto<>("Ok"));
    }

}
