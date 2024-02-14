package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.dto.chapter.ChapterDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.service.ChapterService;
import com.nazar.grynko.learningcourses.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._200;
import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode._404;

@RestController
@RequestMapping("learning-courses/api/v1/chapters")
public class ChapterController {

    private final ChapterService chapterService;
    private final LessonService lessonService;

    public ChapterController(ChapterService chapterService,
                             LessonService lessonService) {
        this.chapterService = chapterService;
        this.lessonService = lessonService;
    }

    @Operation(summary = "Get a chapter",
            description = "Get a chapter by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the chapter"),
            @ApiResponse(responseCode = _404, description = "Chapter not found")
    })
    @GetMapping("/chapter")
    ResponseEntity<ChapterDto> one(@RequestParam Long chapterId) {
        return ResponseEntity.ok(chapterService.get(chapterId));
    }

    @Operation(summary = "Get chapter's lessons",
            description = "Get a list of lessons for a specific chapter. List of lessons is retrieved by the chapter id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all the lessons of the chapter"),
            @ApiResponse(responseCode = _404, description = "Chapter not found")
    })
    @GetMapping("/chapter/lessons")
    ResponseEntity<List<LessonDto>> allLessonsInChapter(@RequestParam Long chapterId) {
        return ResponseEntity.ok(lessonService.getAllInChapter(chapterId));
    }

    @Operation(summary = "Get chapter's all user-lesson performance",
            description = "Get a list of all user-lessons performance records for a specific chapter. List of user-lesson performance is retrieved by lesson id. Usually is used to displace in drop-down list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all user-lesson performance for the chapter"),
            @ApiResponse(responseCode = _404, description = "Chapter not found")
    })
    @GetMapping("/chapter/users")
    ResponseEntity<List<UserToLessonDto>> allUsersToLessonsInChapter(@RequestParam Long chapterId) {
        return ResponseEntity.ok(lessonService.getAllUserToLessonInChapter(chapterId));
    }

    @Operation(summary = "Delete a chapter",
            description = "Delete a chapter by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Chapter deleted"),
            @ApiResponse(responseCode = _404, description = "Chapter not found")
    })
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @DeleteMapping("/chapter")
    void delete(@RequestParam Long chapterId) {
        chapterService.delete(chapterId);
    }

/*    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @PostMapping
    ResponseEntity<ChapterDto> save(@RequestParam Long courseId, @RequestBody ChapterDtoSave chapterDto) {
        return ResponseEntity.ok(chapterService.save(chapterDto, courseId));
    }*/

    @Operation(summary = "Update a chapter",
            description = "Update chapter's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Chapter updated"),
            @ApiResponse(responseCode = _404, description = "Chapter not found")
    })
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @PutMapping("/chapter")
    ResponseEntity<ChapterDto> update(@RequestParam Long chapterId,
                                      @Valid @RequestBody ChapterDtoUpdate chapterDto) {
        return ResponseEntity.ok(chapterService.update(chapterDto, chapterId));
    }

}
