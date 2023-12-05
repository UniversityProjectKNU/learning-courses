package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.security.JwtAuthenticationFilter;
import com.nazar.grynko.learningcourses.service.LessonService;
import com.nazar.grynko.learningcourses.service.UserToLessonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.nazar.grynko.learningcourses.util.MockEntities.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LessonController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LessonControllerTest {

    private final static String BASE_URL = "/learning-courses/api/v1/lessons";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
        when(lessonService.get(any())).thenReturn(mockLessonDto());
        this.mockMvc.perform(get(BASE_URL + "/lesson")
                        .param("lessonId", String.valueOf(1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void one_400_noSuchLesson() throws Exception {
        when(lessonService.get(any())).thenThrow(new IllegalArgumentException());
        this.mockMvc.perform(get(BASE_URL + "/lesson")
                        .param("lessonId", String.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsersLessonsInfo_200_checkResult() throws Exception {
        when(lessonService.getAllUserToLessonInfoForLesson(any())).thenReturn(List.of(mockUsersToLessonDto()));
        this.mockMvc.perform(get(BASE_URL + "/lesson/users")
                        .param("lessonId", String.valueOf(1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsersLessonsInfo_400_noSuchLesson() throws Exception {
        when(lessonService.getAllUserToLessonInfoForLesson(any())).thenThrow(new IllegalArgumentException());
        this.mockMvc.perform(get(BASE_URL + "/lesson/users")
                        .param("lessonId", String.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUsersLessonInfo_200_checkResult() throws Exception {
        when(lessonService.getUsersLessonInfo(any(), any())).thenReturn(mockUsersToLessonDto());
        this.mockMvc.perform(get(BASE_URL + "/lesson/users/user")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUsersLessonInfo_400_noSuchUserToLesson() throws Exception {
        when(lessonService.getUsersLessonInfo(any(), any())).thenThrow(new IllegalArgumentException());
        this.mockMvc.perform(get(BASE_URL + "/lesson/users/user")
                        .param("lessonId", "-1")
                        .param("userId", "-1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getHomeworkInfo_200_checkResult() throws Exception {
        when(userToLessonService.getFileInfo(any(), any())).thenReturn(mockHomeworkFileDto());
        this.mockMvc.perform(get(BASE_URL + "/lesson/files/file/info")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getHomeworkInfo_200_noFileWasFound() throws Exception {
        when(userToLessonService.getFileInfo(any(), any())).thenReturn(null);
        this.mockMvc.perform(get(BASE_URL + "/lesson/users/user")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void upload_200_checkResult() throws Exception {
        when(userToLessonService.uploadFile(any(), any(), any())).thenReturn(mockHomeworkFileDto());
        this.mockMvc.perform(multipart(BASE_URL + "/lesson/files")
                        .file(mockMultipartFile())
                        .principal(mockPrincipal())
                        .param("lessonId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void upload_400_errorWithFile() throws Exception {
        when(userToLessonService.uploadFile(any(), any(), any())).thenThrow(new IllegalArgumentException());
        this.mockMvc.perform(multipart(BASE_URL + "/lesson/files")
                        .file(mockMultipartFile())
                        .principal(mockPrincipal())
                        .param("lessonId", "-1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void download_200_checkResult() throws Exception {
        when(userToLessonService.downloadFile(any(), any())).thenReturn(mockFileDto());
        this.mockMvc.perform(get(BASE_URL + "/lesson/files/file")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-type", "application/octet-stream"))
                .andExpect(header().string("Content-disposition", "attachment; filename=\"" + mockFileDto().getTitle() + "\""));
    }

    @Test
    void download_400_errorWithFile() throws Exception {
        when(userToLessonService.downloadFile(any(), any())).thenThrow(new IllegalArgumentException());
        this.mockMvc.perform(get(BASE_URL + "/lesson/files/file")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_200_checkResult() throws Exception {
        this.mockMvc.perform(delete(BASE_URL + "/lesson/files/file")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void delete_400_errorWithFile() throws Exception {
        doThrow(new RuntimeException()).when(userToLessonService).deleteFile(any(), anyLong());
        this.mockMvc.perform(delete(BASE_URL + "/lesson/files/file")
                        .param("lessonId", "1")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
