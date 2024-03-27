package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoUpdate;
import com.nazar.grynko.learningcourses.dto.simple.SimpleDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDtoUpdate;
import com.nazar.grynko.learningcourses.service.LessonService;
import com.nazar.grynko.learningcourses.service.UserToLessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode.*;

@RestController //todo default roles
@RequestMapping("learning-courses/api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final UserToLessonService userToLessonService;

    public LessonController(LessonService lessonService,
                            UserToLessonService userToLessonService) {
        this.lessonService = lessonService;
        this.userToLessonService = userToLessonService;
    }

    @Operation(summary = "Get a lesson",
            description = "Get a lesson by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the lesson"),
            @ApiResponse(responseCode = _404, description = "Lesson not found")
    })
    @GetMapping("/lesson")
    ResponseEntity<LessonDto> one(@RequestParam Long lessonId) {
        return ResponseEntity.ok(lessonService.get(lessonId));
    }

    @Operation(summary = "Get lesson's all student-lesson performance",
            description = "Get a list of all student-lessons performance records for a specific lesson. List of student-lesson performance is retrieved by lesson id. Usually is used to displace in drop-down list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all student-lesson performance for the lesson"),
            @ApiResponse(responseCode = _404, description = "Lesson not found")
    })
    @GetMapping("/lesson/users")
    ResponseEntity<List<UserToLessonDto>> getAllUsersLessonsInfo(@RequestParam Long lessonId) {
        return ResponseEntity.ok(lessonService.getAllUserToLessonInfoForLesson(lessonId));
    }

    @Operation(summary = "Get a specific student-lesson performance",
            description = "Get a specific student-lesson performance of a lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found student-course performance"),
            @ApiResponse(responseCode = _404, description = "Lesson not found / Student not found / No such Student with Lesson")
    })
    @GetMapping("/lesson/users/user")
    ResponseEntity<UserToLessonDto> getUserToLessonInfo(@RequestParam Long lessonId,
                                                        @RequestParam Long userId) {
        return ResponseEntity.ok(lessonService.getUsersLessonInfo(lessonId, userId));
    }

    @Operation(summary = "Update student-lesson performance",
            description = "Update student-lesson performance of a lesson. Usually to assign a mark")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Updated student-lesson performance"),
            @ApiResponse(responseCode = _404, description = "Lesson not found / Student not found / No such Student with Lesson")
    })
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @PutMapping("/lesson/users/user")
    ResponseEntity<UserToLessonDto> updateUserToLessonInfo(@RequestParam Long lessonId,
                                                           @RequestParam Long userId,
                                                           @Valid @RequestBody UserToLessonDtoUpdate userToLessonDtoUpdate) {
        return ResponseEntity.ok(lessonService.updateUserToLesson(lessonId, userId, userToLessonDtoUpdate));
    }

    @Operation(summary = "Delete a lesson",
            description = "Delete a lesson by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Lesson deleted"),
            @ApiResponse(responseCode = _404, description = "Lesson not found")
    })
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @DeleteMapping("/lesson")
    void delete(@RequestParam Long lessonId) {
        lessonService.delete(lessonId);
    }

/*    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @PostMapping
    ResponseEntity<LessonDto> save(@RequestParam Long chapterId, @RequestBody LessonDtoSave lessonDto) {
        return ResponseEntity.ok(lessonService.save(lessonDto, chapterId));
    }*/

    @Operation(summary = "Update a lesson",
            description = "Update lesson's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Lesson updated"),
            @ApiResponse(responseCode = _404, description = "Lesson not found")
    })
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @PutMapping("/lesson")
    ResponseEntity<LessonDto> update(@RequestParam Long lessonId,
                                     @Valid @RequestBody LessonDtoUpdate lessonDto) {
        return ResponseEntity.ok(lessonService.update(lessonDto, lessonId));
    }

    @Operation(summary = "Finish a lesson",
            description = "Finish the lesson. When lesson is finished nobody can send attach files to lesson or rate it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Lesson finished"),
            @ApiResponse(responseCode = _400, description = "Lesson is already finished"),
            @ApiResponse(responseCode = _404, description = "Lesson not found")
    })
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @PutMapping("/lesson/finish")
    ResponseEntity<LessonDto> finishLesson(@RequestParam Long lessonId) {
        return ResponseEntity.ok(lessonService.finish(lessonId));
    }

    @Operation(summary = "Get a student's homework meta information",
            description = "Get a student's homework meta information by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the homework or null"),
            @ApiResponse(responseCode = _404, description = "Lesson not found / Student not found")
    })
    @GetMapping("/lesson/files/file/info")
    public ResponseEntity<HomeworkFileDto> getHomeworkInfo(@RequestParam Long lessonId,
                                                           @RequestParam Long userId) {
        return ResponseEntity.ok(userToLessonService.getFileInfo(lessonId, userId));
    }

    @Operation(summary = "Upload a student's homework to lesson",
            description = "Upload a student's homework to lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Homework uploaded"),
            @ApiResponse(responseCode = _400, description = "Lesson is already finished"),
            @ApiResponse(responseCode = _404, description = "Lesson not found / Student not found / No such Student with Lesson")
    })
    @PostMapping("/lesson/files")
    public ResponseEntity<HomeworkFileDto> upload(@RequestParam Long lessonId,
                                                  @RequestBody MultipartFile file,
                                                  Principal principal) {
        return ResponseEntity.ok(userToLessonService.uploadFile(lessonId, principal.getName(), file));
    }

    @Operation(summary = "Download a student's homework",
            description = "Download a student's homework. Lesson can be finished")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Homework downloaded"),
            @ApiResponse(responseCode = _404, description = "Lesson not found / Student not found / No such Student with Lesson")
    })
    @GetMapping("/lesson/files/file")
    public ResponseEntity<ByteArrayResource> download(@RequestParam Long lessonId,
                                                      @RequestParam Long userId) {
        var fileDto = userToLessonService.downloadFile(lessonId, userId);
        var data = fileDto.getData();

        var headerTitle = URLEncoder.encode(fileDto.getTitle(), StandardCharsets.UTF_8);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + headerTitle + "\"")
                .body(new ByteArrayResource(data));
    }

    //TODO implement this method for student only
/*    @DeleteMapping("/lesson/files/file")
    public ResponseEntity<SimpleDto<String>> delete(@RequestParam Long lessonId, Principal principal) {
        if (lessonService.isFinished(lessonId)) {
            throw new InvalidPathException("You cannot upload file in finished lesson");
        }

        userToLessonService.delete(lessonId, principal.getName());
        return ResponseEntity.ok(new SimpleDto<>("Ok"));
    }*/

    @Operation(summary = "Delete a student's homework",
            description = "Delete a student's homework. Cannot be deleted from finished lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Homework deleted"),
            @ApiResponse(responseCode = _400, description = "Lesson is already finished"),
            @ApiResponse(responseCode = _404, description = "Lesson not found / Student not found / No such Student with Lesson")
    })
    @DeleteMapping("/lesson/files/file")
    public ResponseEntity<SimpleDto<String>> delete(@RequestParam Long lessonId,
                                                    @RequestParam Long userId) {
        userToLessonService.deleteFile(lessonId, userId);
        return ResponseEntity.ok(new SimpleDto<>("Ok"));
    }

}
