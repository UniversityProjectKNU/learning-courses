package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.mapper.HomeworkFileMapper;
import com.nazar.grynko.learningcourses.model.UserToLesson;
import com.nazar.grynko.learningcourses.service.internal.UserToLessonInternalService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class UserToLessonService {

    private final UserToLessonInternalService userToLessonInternalService;
    private final HomeworkFileMapper homeworkFileMapper;

    public UserToLessonService(UserToLessonInternalService userToLessonInternalService, HomeworkFileMapper homeworkFileMapper) {
        this.userToLessonInternalService = userToLessonInternalService;
        this.homeworkFileMapper = homeworkFileMapper;
    }

    public HomeworkFileDto getFileInfo(Long lessonId, Long studentId) {
        var entity = userToLessonInternalService.getFileInfo(lessonId, studentId);
        return nonNull(entity) ? homeworkFileMapper.toDto(entity) : null;
    }

    public HomeworkFileDto uploadFile(Long lessonId, String login, MultipartFile file) {
        var entity = userToLessonInternalService.uploadFile(lessonId, login, file);
        return homeworkFileMapper.toDto(entity);
    }

    public FileDto downloadFile(Long lessonId, Long userId) {
        return userToLessonInternalService.downloadFile(lessonId, userId);
    }

    public void deleteFile(Long lessonId, String login) {
        userToLessonInternalService.deleteFile(lessonId, login);
    }

    public void deleteFile(Long lessonId, Long userId) {
        userToLessonInternalService.deleteFile(lessonId, userId);
    }

    public List<UserToLesson> getAll(Long courseId, String login) {
        return userToLessonInternalService.getAllByUserLoginAndCourseId(login, courseId);
    }
}
