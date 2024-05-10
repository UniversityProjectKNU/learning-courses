package com.nazar.grynko.learningcourses.util;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserSecurityDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.model.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.security.Principal;

public interface MockEntities {

    ///////////////////////////////////////////////////
    // MOCK_OTHER
    ///////////////////////////////////////////////////

    static Principal mockPrincipal() {
        return () -> "test.login@gmail.com";
    }

    static MockMultipartFile mockMultipartFile() {
        return new MockMultipartFile(
                "test",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Test text.".getBytes()
        );
    }

    ///////////////////////////////////////////////////
    // MOCK_DTO
    ///////////////////////////////////////////////////

    static UserSecurityDto mockUserSecurityDto() {
        return new UserSecurityDto()
                .setId(1L)
                .setRole(RoleType.STUDENT)
                .setLogin("test.user@gmail.com")
                .setToken("token");
    }

    static UserDto mockUserDto() {
        return new UserDto()
                .setId(1L)
                .setRole(RoleType.STUDENT)
                .setLogin("test.user@gmail.com")
                .setFirstName("Nazar")
                .setLastName("Grynko");
    }

    static LessonDto mockLessonCorrectDto() {
        return new LessonDto()
                .setId(1L)
                .setChapterId(2L)
                .setCourseId(3L)
                .setIsFinished(false)
                .setNumber("5")
                .setMaxMark(20)
                .setSuccessMark(10);
    }

    static LessonDto mockLessonFinishedDto() {
        return new LessonDto()
                .setId(2L)
                .setChapterId(3L)
                .setCourseId(4L)
                .setIsFinished(true)
                .setNumber("6")
                .setMaxMark(20)
                .setSuccessMark(10);
    }

    static UserToLessonDto mockUsersToLessonDto() {
        return new UserToLessonDto()
                .setId(1L)
                .setUserId(2L)
                .setLessonId(3L)
                .setIsPassed(false)
                .setMark(10);
    }

    static HomeworkFileDto mockHomeworkFileDto() {
        return new HomeworkFileDto()
                .setId(1L)
                .setSize(100L)
                .setS3Name("test.s3Name")
                .setTitle("testTitle");
    }

    static FileDto mockFileDto() {
        return new FileDto()
                .setData(new byte[]{1, 1, 1, 1, 1})
                .setS3Name("test.s3Name")
                .setTitle("testTitle");
    }

    ///////////////////////////////////////////////////
    // MOCK_ENTITY
    ///////////////////////////////////////////////////

    static HomeworkFile mockHomeWorkFile() {
        return new HomeworkFile()
                .setId(100L)
                .setUserToLesson(new UserToLesson())
                .setSize(100L)
                .setS3Name("test.s3Name")
                .setTitle("testTitle");
    }

    static UserToLesson mockUsersToLesson() {
        return new UserToLesson()
                .setId(100L)
                .setUser(new User())
                .setLesson(new Lesson())
                .setIsPassed(false)
                .setMark(10);
    }

    static Course mockCourse() {
        return new Course()
                .setId(1L)
                .setTitle("testTitle")
                .setDescription("testDescription")
                .setIsFinished(false);
    }
}
