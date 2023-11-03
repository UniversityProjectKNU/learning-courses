package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.mapper.HomeworkFileMapper;
import com.nazar.grynko.learningcourses.model.HomeworkFile;
import com.nazar.grynko.learningcourses.model.UserToLesson;
import com.nazar.grynko.learningcourses.service.internal.UserToLessonInternalService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        return homeworkFileMapper.toDto(entity);
    }

    public HomeworkFileDto upload(Long lessonId, String login, MultipartFile file) {
        var entity = userToLessonInternalService.upload(lessonId, login, file);
        return homeworkFileMapper.toDto(entity);
    }

    public FileDto download(Long lessonId, Long userId) {
        return userToLessonInternalService.download(lessonId, userId);
    }

    public void delete(Long lessonId, String login) {
        userToLessonInternalService.delete(lessonId, login);
    }

    public void delete(Long lessonId, Long userId) {
        userToLessonInternalService.delete(lessonId, userId);
    }

    public List<UserToLesson> getAll(Long courseId, String login) {
        return userToLessonInternalService.getAllByUserLoginAndCourseId(login, courseId);
    }
}
