package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoUpdate;
import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.security.JwtAuthenticationFilter;
import com.nazar.grynko.learningcourses.service.LessonService;
import com.nazar.grynko.learningcourses.service.UserToLessonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.nazar.grynko.learningcourses.util.MockEntities.*;
import static com.nazar.grynko.learningcourses.util.RequestJson.LESSON_CORRECT;
import static com.nazar.grynko.learningcourses.util.RequestJson.LESSON_INCORRECT;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LessonControllerTest {

    private final static String BASE_URL = "/learning-courses/api/v1/lessons";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter filter;

    @MockBean
    private LessonService lessonService;
    @MockBean
    private UserToLessonService userToLessonService;

    @Test
    void one_200_checkResult() throws Exception {
        when(lessonService.get(eq(1L))).thenReturn(mockLessonCorrectDto());

        mockMvc.perform(get(BASE_URL + "/lesson")
                        .param("lessonId", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chapterId").value(2L))
                .andExpect(jsonPath("$.courseId").value(3L))
                .andExpect(jsonPath("$.isFinished").value(false))
                .andExpect(jsonPath("$.number").value("5"))
                .andExpect(jsonPath("$.maxMark").value(20))
                .andExpect(jsonPath("$.successMark").value(10));
    }

    @Test
    void one_400_noSuchLesson() throws Exception {
        when(lessonService.get(eq(-1L))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get(BASE_URL + "/lesson")

                        .param("lessonId", String.valueOf(-1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsersLessonsInfo_200_checkResult() throws Exception {
        when(lessonService.getAllUserToLessonInfoForLesson(eq(1L)))
                .thenReturn(List.of(mockUsersToLessonDto()));

        mockMvc.perform(get(BASE_URL + "/lesson/users")
                        .param("lessonId", String.valueOf(1)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsersLessonsInfo_400_noSuchLesson() throws Exception {
        when(lessonService.getAllUserToLessonInfoForLesson(eq(-1L)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(get(BASE_URL + "/lesson/users")
                        .param("lessonId", String.valueOf(-1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUsersLessonInfo_200_checkResult() throws Exception {
        when(lessonService.getUsersLessonInfo(eq(1L), eq(1L)))
                .thenReturn(mockUsersToLessonDto());

        mockMvc.perform(get(BASE_URL + "/lesson/users/user")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(2L))
                .andExpect(jsonPath("$.lessonId").value(3L))
                .andExpect(jsonPath("$.isPassed").value(false))
                .andExpect(jsonPath("$.mark").value(10));
    }

    @Test
    void getUsersLessonInfo_400_noSuchUserToLesson() throws Exception {
        when(lessonService.getUsersLessonInfo(eq(-1L), eq(-1L)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(get(BASE_URL + "/lesson/users/user")
                        .param("lessonId", "-1")
                        .param("userId", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_200_checkResult() throws Exception {
        when(lessonService.update(any(), any())).thenReturn(mockLessonCorrectDto());

        mockMvc.perform(put(BASE_URL + "/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("lessonId", "1")
                        .content(LESSON_CORRECT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chapterId").value(2L))
                .andExpect(jsonPath("$.courseId").value(3L))
                .andExpect(jsonPath("$.isFinished").value(false))
                .andExpect(jsonPath("$.number").value("5"))
                .andExpect(jsonPath("$.maxMark").value(20))
                .andExpect(jsonPath("$.successMark").value(10));
    }

    @Test
    void update_400_invalidFormat() throws Exception {
        when(lessonService.update(any(LessonDtoUpdate.class), eq(1L))).thenThrow(new EntityNotFoundException());

        mockMvc.perform(put(BASE_URL + "/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("lessonId", "1")
                        .content(LESSON_INCORRECT))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_404_noSuchLesson() throws Exception {
        when(lessonService.update(any(LessonDtoUpdate.class), eq(1L))).thenThrow(new EntityNotFoundException());

        mockMvc.perform(put(BASE_URL + "/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("lessonId", "1")
                        .content(LESSON_CORRECT))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHomeworkInfo_200_checkResult() throws Exception {
        when(userToLessonService.getFileInfo(eq(1L), eq(1L)))
                .thenReturn(mockHomeworkFileDto());

        mockMvc.perform(get(BASE_URL + "/lesson/files/file/info")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.size").value(100L))
                .andExpect(jsonPath("$.s3Name").value("test.s3Name"))
                .andExpect(jsonPath("$.title").value("testTitle"));
    }

    @Test
    void getHomeworkInfo_200_noFileWasFound() throws Exception {
        when(userToLessonService.getFileInfo(any(), any())).thenReturn(null);

        mockMvc.perform(get(BASE_URL + "/lesson/users/user")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void finishLesson_200_checkResult() throws Exception {
        when(lessonService.finish(any())).thenReturn(mockLessonFinishedDto());

        mockMvc.perform(put(BASE_URL + "/lesson/finish")
                        .param("lessonId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.chapterId").value(3L))
                .andExpect(jsonPath("$.courseId").value(4L))
                .andExpect(jsonPath("$.isFinished").value(true))
                .andExpect(jsonPath("$.number").value("6"))
                .andExpect(jsonPath("$.maxMark").value(20))
                .andExpect(jsonPath("$.successMark").value(10));
    }

    @Test
    void finishLesson_200_noFileWasFound() throws Exception {
        when(lessonService.finish(any()))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(put(BASE_URL + "/lesson/finish")
                        .param("lessonId", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void upload_200_checkResult() throws Exception {
        when(userToLessonService.uploadFile(any(), any(), any())).thenReturn(mockHomeworkFileDto());

        mockMvc.perform(multipart(BASE_URL + "/lesson/files")
                        .file(mockMultipartFile())
                        .principal(mockPrincipal())
                        .param("lessonId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.size").value(100L))
                .andExpect(jsonPath("$.s3Name").value("test.s3Name"))
                .andExpect(jsonPath("$.title").value("testTitle"));
    }

    @Test
    void upload_400_errorWithFile() throws Exception {
        when(userToLessonService.uploadFile(any(), any(), any()))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(multipart(BASE_URL + "/lesson/files")
                        .file(mockMultipartFile())
                        .principal(mockPrincipal())
                        .param("lessonId", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void download_200_checkResult() throws Exception {
        when(userToLessonService.downloadFile(any(), any()))
                .thenReturn(mockFileDto());

        mockMvc.perform(get(BASE_URL + "/lesson/files/file")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-type", "application/octet-stream"))
                .andExpect(header().string("Content-disposition", "attachment; filename=\"" + mockFileDto().getTitle() + "\""));
    }

    @Test
    void download_400_errorWithFile() throws Exception {
        when(userToLessonService.downloadFile(any(), any()))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(get(BASE_URL + "/lesson/files/file")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_200_checkResult() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/lesson/files/file")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_400_errorWithFile() throws Exception {
        doThrow(new RuntimeException()).when(userToLessonService).deleteFile(any(), anyLong());
        mockMvc.perform(delete(BASE_URL + "/lesson/files/file")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andExpect(status().isBadRequest());
    }

}
