package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.simple.SimpleDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseInfoDto;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.service.ChapterService;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.nazar.grynko.learningcourses.controller.enums.ResponseCode.*;

@RestController
@RequestMapping("learning-courses/api/v1/courses")
public class CourseController {

    private final CourseService courseService;
    private final ChapterService chapterService;
    private final LessonService lessonService;

    public CourseController(CourseService courseService,
                            ChapterService chapterService,
                            LessonService lessonService) {
        this.courseService = courseService;
        this.chapterService = chapterService;
        this.lessonService = lessonService;
    }

    @Operation(summary = "Get a course",
            description = "Get a course by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found the course"),
            @ApiResponse(responseCode = _404, description = "Course not found")
    })
    @GetMapping("/course")
    ResponseEntity<CourseDto> one(@RequestParam Long courseId) {
        return ResponseEntity.ok(courseService.get(courseId));
    }

    @Operation(summary = "Get all course",
            description = "Get a list of all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all courses")
    })
    @GetMapping
    ResponseEntity<List<CourseDto>> all(@RequestParam(required = false) Boolean isActive) {
        return ResponseEntity.ok(courseService.getAll(isActive));
    }

    @Operation(summary = "Get course's lessons",
            description = "Get a list of all lessons for a specific course. List of lessons is retrieved by the course id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all the lessons of the course"),
            @ApiResponse(responseCode = _404, description = "Parent course not found")
    })
    @GetMapping("/course/lessons")
    ResponseEntity<List<LessonDto>> allLessonsInCourse(@RequestParam Long courseId) {
        return ResponseEntity.ok(lessonService.getAllInCourse(courseId));
    }

    @Operation(summary = "Get course's chapters",
            description = "Get a list of all chapters for a specific course. List of chapters is retrieved by the course id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all the chapter of the course"),
            @ApiResponse(responseCode = _404, description = "Course not found")
    })
    @GetMapping("/course/chapters")
    ResponseEntity<List<ChapterDto>> allChaptersInCourse(@RequestParam Long courseId) {
        return ResponseEntity.ok(chapterService.getAllInCourse(courseId));
    }

    @Operation(summary = "Delete a course",
            description = "Delete a course by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Course deleted"),
            @ApiResponse(responseCode = _404, description = "Course not found")
    })
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @DeleteMapping("/course")
    void delete(@RequestParam Long courseId) {
        courseService.delete(courseId);
    }

    /*@RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PostMapping
    ResponseEntity<CourseDto> save(@RequestBody CourseDtoSave courseDto, Principal principal) {
        return ResponseEntity.ok(courseService.save(courseDto*//*, principal.getName()*//*));
    }*/

    @Operation(summary = "Update a course",
            description = "Update course's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Course updated"),
            @ApiResponse(responseCode = _404, description = "Course not found")
    })
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/course")
    ResponseEntity<CourseDto> update(@RequestParam Long courseId,
                                     @Valid @RequestBody CourseDtoUpdate courseDto) {
        return ResponseEntity.ok(courseService.update(courseDto, courseId));
    }

    @Operation(summary = "Finish a course",
            description = "Finish the course. When course is finished nobody can send enroll requests, attach files to lessons or rate lessons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Course finished"),
            @ApiResponse(responseCode = _400, description = "Course is already finished"),
            @ApiResponse(responseCode = _404, description = "Course not found")
    })
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/course/finish")
    ResponseEntity<CourseDto> finishCourse(@RequestParam Long courseId) {
        return ResponseEntity.ok(courseService.finish(courseId));
    }

    @Operation(summary = "Send enroll request to a course",
            description = "Student or instructor, which is not enrolled in the course, can send request to join it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Course updated"),
            @ApiResponse(responseCode = _400, description = "User already enrolled / User already has an active request / Student has maximum amount of active courses"),
            @ApiResponse(responseCode = _404, description = "Course not found / User not found")
    })
    @RolesAllowed({"STUDENT", "INSTRUCTOR"})
    @PostMapping("/course/enrolls")
    ResponseEntity<?> sendEnrollRequest(@RequestParam Long courseId, Principal principal) {
        return ResponseEntity.ok(courseService.sendEnrollRequest(courseId, principal.getName()));
    }

    @Operation(summary = "Get course's enroll requests",
            description = "Get a list of all enroll requests for a specific course. List of enroll requests is retrieved by the course id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all the enroll requests of the course"),
            @ApiResponse(responseCode = _404, description = "Course not found")
    })
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @GetMapping("/course/enrolls")
    ResponseEntity<?> getAllEnrollRequests(@RequestParam Long courseId,
                                           @RequestParam(required = false) Boolean isActive) {
        return ResponseEntity.ok(courseService.getAllEnrollRequests(courseId, isActive));
    }

    @Operation(summary = "Approve enroll request",
            description = "Approve enroll request by its id and flag is it approved or denied. If user's role is STUDENT, then we create one userToCourse and many userToLesson entities. Else if it is INSTRUCTOR, then we create only one userToCourse entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Approved/denied enroll request"),
            @ApiResponse(responseCode = _400, description = "You cannot enroll ADMIN / User already is enrolled to this course / Student has maximum amount of active courses"),
            @ApiResponse(responseCode = _404, description = "Course not found")
    })
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/course/enrolls/enroll")
    ResponseEntity<?> approveEnrollRequest(@RequestParam Long enrollRequestId,
                                           @RequestParam Boolean isApproved) {
        return ResponseEntity.ok(courseService.approveEnrollRequest(enrollRequestId, isApproved));
    }

    @Operation(summary = "Assign user to course without approval",
            description = "Admin role method to assign a user to a specific course directly without enroll process. If user's role is STUDENT, then we create one userToCourse and many userToLesson entities. Else if it is INSTRUCTOR, then we create only one userToCourse entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "User assigned"),
            @ApiResponse(responseCode = _400, description = "You cannot enroll ADMIN / User already is enrolled to this course / Student has maximum amount of active courses"),
            @ApiResponse(responseCode = _404, description = "Course not found / User not found")
    })
    //TODO @RolesAllowed("ADMIN")
    @PostMapping("/course/users/enrolls")
    ResponseEntity<UserToCourseDto> assignUserToCourse(@RequestParam Long courseId,
                                                       @RequestParam Long userId) {
        return ResponseEntity.ok(courseService.assignUserToCourse(courseId, userId));
    }

    @Operation(summary = "Get user's last enroll request",
            description = "Returns user's last enroll request. It can be active request, or previously processed one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Enroll request is returned if exist, otherwise null"),
            @ApiResponse(responseCode = _404, description = "Course not found / User not found")
    })
    @RolesAllowed({"STUDENT", "INSTRUCTOR"})
    @GetMapping("/course/users/enrolls/enroll")
    ResponseEntity<?> getUsersLastEnrollRequestForCourse(@RequestParam Long userId,
                                                         @RequestParam Long courseId) {
        return ResponseEntity.ok(courseService.getUsersLastEnrollRequest(userId, courseId));
    }

    @Operation(summary = "Get course's user-course performance",
            description = "Get a list of all user-course performance for a specific course. List of user-course performance is retrieved by the course id and role type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found all the chapter of the course"),
            @ApiResponse(responseCode = _400, description = "RoleType cannot be ADMIN"),
            @ApiResponse(responseCode = _404, description = "Course not found")
    })
    // It meant to return information for all users and only small part of it (only users), but it was very handy for general use.
    // Probably we should separate getAllUsersForCourse and getAllUsersToCourseInfo
    @GetMapping("/course/users")
    ResponseEntity<List<UserToCourseInfoDto>> allInfoOfUsersInCourse(@RequestParam Long courseId,
                                                                     @RequestParam(required = false) RoleType roleType) {
        return ResponseEntity.ok(courseService.getAllUserToCourseInfoForCourse(courseId, roleType));
    }

    @Operation(summary = "Get a specific user-course performance",
            description = "Get a specific user-course performance of a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Found user-course performance"),
            @ApiResponse(responseCode = _404, description = "Course not found / User not found / No such User with Course")
    })
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    @GetMapping("/course/users/user")
    ResponseEntity<UserToCourseDto> getUserCoursePerformanceInfo(@RequestParam Long courseId,
                                                                 @RequestParam Long userId) {
        return ResponseEntity.ok(courseService.getUsersCourseInfo(courseId, userId));
    }

    @Operation(summary = "Update user-course performance",
            description = "Update user-course performance of a course. Usually to provide final feedback")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Updated user-course performance"),
            @ApiResponse(responseCode = _404, description = "Course not found / User not found")
    })
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @PutMapping("/course/users/user")
    ResponseEntity<UserToCourseDto> updateUserCoursePerformanceInfo(@RequestParam Long courseId,
                                                                    @RequestParam Long userId,
                                                                    @Valid @RequestBody UserToCourseDtoUpdate userToCourseDto) {
        return ResponseEntity.ok(courseService.updateUserToCourse(courseId, userId, userToCourseDto));
    }

    @Operation(summary = "Remove user from course",
            description = "Remove user from course, except course creator. If user is remove all their course related data is deleted as well")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "User removed from course"),
            @ApiResponse(responseCode = _400, description = "Cannot remove course owner"),
            @ApiResponse(responseCode = _404, description = "Course not found / User not found")
    })
    @RolesAllowed({"INSTRUCTOR", "ADMIN"})
    @DeleteMapping("/course/users/user")
    ResponseEntity<?> removeUserFromCourse(@RequestParam Long courseId,
                                           @RequestParam Long userId) {
        courseService.removeUserFromCourse(courseId, userId);
        return ResponseEntity.ok(new SimpleDto<>("OK"));
    }

    @Operation(summary = "Get course owner",
            description = "Get course owner by course id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = _200, description = "Owner found"),
            @ApiResponse(responseCode = _404, description = "Course not found")
    })
    @GetMapping("/course/users/owner")
    ResponseEntity<?> getCourseOwner(@RequestParam Long courseId) {
        return ResponseEntity.ok(courseService.getCourseOwner(courseId));
    }

}
