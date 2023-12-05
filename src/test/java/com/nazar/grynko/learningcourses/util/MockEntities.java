package com.nazar.grynko.learningcourses.util;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.security.SignInDto;
import com.nazar.grynko.learningcourses.dto.security.SignUpDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserSecurityDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.model.RoleType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.security.Principal;

public interface MockEntities {

    static Principal mockPrincipal() {
        return () -> "test.login@gmail.com";
    }

    static SignInDto mockSignInDto() {
        return new SignInDto()
                .setLogin("test.user@gmail.com")
                .setPassword("test.password");
    }

    static SignUpDto mockSignUpDto() {
        return new SignUpDto()
                .setLogin("test.user@gmail.com")
                .setPassword("test.password")
                .setFirstName("testFirstName")
                .setLastName("testLastName");
    }

    static UserSecurityDto mockUserSecurityDto() {
        return new UserSecurityDto()
                .setId(100L)
                .setRole(RoleType.STUDENT)
                .setLogin("test.user@gmail.com")
                .setToken("token");
    }

    static UserDto mockUserDto() {
        return new UserDto()
                .setId(100L)
                .setRole(RoleType.STUDENT)
                .setLogin("test.user@gmail.com")
                .setFirstName("testFirstName")
                .setLastName("testLastName");
    }

    static LessonDto mockLessonDto() {
        return new LessonDto()
                .setId(100L)
                .setChapterId(100L)
                .setCourseId(100L)
                .setIsFinished(false)
                .setNumber("100")
                .setMaxMark(20)
                .setSuccessMark(10);
    }

    static UserToLessonDto mockUsersToLessonDto() {
        return new UserToLessonDto()
                .setId(100L)
                .setUserId(100L)
                .setLessonId(100L)
                .setIsPassed(false)
                .setMark(10);
    }

    static HomeworkFileDto mockHomeworkFileDto() {
        return new HomeworkFileDto()
                .setId(100L)
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

    static MockMultipartFile mockMultipartFile() {
        return new MockMultipartFile(
                "test",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Test text.".getBytes()
        );
    }

}
