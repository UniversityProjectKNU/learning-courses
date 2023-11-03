package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoSave;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoUpdate;
import com.nazar.grynko.learningcourses.dto.simple.SimpleDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
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
@RequestMapping("learning-courses/api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final UserToLessonService userToLessonService;

    public LessonController(LessonService lessonService,
                            UserToLessonService userToLessonService) {
        this.lessonService = lessonService;
        this.userToLessonService = userToLessonService;
    }

    @GetMapping("/lesson")
    ResponseEntity<LessonDto> one(@RequestParam Long lessonId) {
        return ResponseEntity.ok(lessonService.get(lessonId)
                .orElseThrow(InvalidPathException::new));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @DeleteMapping("/lesson")
    void delete(@RequestParam Long lessonId) {
        lessonService.delete(lessonId);
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PostMapping
    ResponseEntity<LessonDto> save(@RequestParam Long chapterId, @RequestBody LessonDtoSave lessonDto) {
        return ResponseEntity.ok(lessonService.save(lessonDto, chapterId));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/lesson")
    ResponseEntity<LessonDto> update(@RequestParam Long lessonId, @RequestBody LessonDtoUpdate lessonDto) {
        return ResponseEntity.ok(lessonService.update(lessonDto, lessonId));
    }

    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/lesson/finish")
    ResponseEntity<LessonDto> finishLesson(@RequestParam Long lessonId) {
        return ResponseEntity.ok(lessonService.finish(lessonId));
    }


    ///////////////////////////////////////////////////////////////////////////



    //TODO when file's size is bigger then we can allow
    @PostMapping("/lesson/files/upload")
    public ResponseEntity<HomeworkFileDto> upload(@RequestParam Long lessonId, @RequestBody MultipartFile file, Principal principal) {
        if (lessonService.isFinished(lessonId)) {
            throw new InvalidPathException("You cannot upload file in finished lesson");
        }

        return ResponseEntity.ok(userToLessonService.upload(lessonId, principal.getName(), file));
    }

    //TODO delete
    @GetMapping("/lesson/files/info")
    public ResponseEntity<HomeworkFileDto> getFileInfo(@RequestParam Long lessonId, Principal principal) {
        return ResponseEntity.ok(userToLessonService.getFileInfo(lessonId, principal.getName()));
    }

    @GetMapping("/lesson/files/info/{userId}")
    public ResponseEntity<HomeworkFileDto> getStudentFileInfo(@RequestParam Long lessonId, @RequestParam Long userId) {
        return ResponseEntity.ok(userToLessonService.getFileInfo(lessonId, userId));
    }

    //TODO download file by userId and lessonId
    //TODO download file bu fileId
    @GetMapping("/lesson/files/file")
    public ResponseEntity<ByteArrayResource> download(@RequestParam Long lessonId, Principal principal) {
        var fileDto = userToLessonService.download(lessonId, principal.getName());
        var data = fileDto.getData();

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileDto.getTitle() + "\"")
                .body(new ByteArrayResource(data));
    }

    @DeleteMapping("/lesson/files/delete")
    public ResponseEntity<SimpleDto<String>> delete(@RequestParam Long lessonId, Principal principal) {
        if (lessonService.isFinished(lessonId)) {
            throw new InvalidPathException("You cannot upload file in finished lesson");
        }

        userToLessonService.delete(lessonId, principal.getName());
        return ResponseEntity.ok(new SimpleDto<>("Ok"));
    }

//    @GetMapping("/{courseId}/lessons/{lessonId}/users/{userId}")
//    ResponseEntity<UserToLessonDto> getStudentLessonInfo(@PathVariable Long courseId, @PathVariable Long lessonId,
//                                                         @PathVariable Long userId) {
//        if (!lessonService.hasWithCourse(lessonId, courseId)) {
//            throw new InvalidPathException(String.format("Lesson %d in course %d doesn't exist", lessonId, courseId));
//        }
//        return ResponseEntity.ok(lessonService.getStudentLessonInfo(lessonId, userId));
//    }
//
//    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
//    @PutMapping("/{courseId}/lessons/{lessonId}/users/{userId}")
//    ResponseEntity<UserToLessonDto> updateUsersLessonInfo(@PathVariable Long courseId,
//                                                          @PathVariable Long lessonId,
//                                                          @PathVariable Long userId,
//                                                          @RequestBody UserToCourseDtoUpdate userToCourseDto) {
//        if (!lessonService.hasWithCourse(lessonId, courseId)) {
//            throw new InvalidPathException(String.format("Lesson %d in course %d doesn't exist", lessonId, courseId));
//        }
//        return ResponseEntity.ok(lessonService.updateUserToLesson(lessonId, userId, userToCourseDto));
//    }

}
