package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.mapper.HomeworkFileMapper;
import com.nazar.grynko.learningcourses.service.internal.UserToLessonInternalService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserToLessonService {

    private final UserToLessonInternalService userToLessonInternalService;
    private final HomeworkFileMapper homeworkFileMapper;

    public UserToLessonService(UserToLessonInternalService userToLessonInternalService, HomeworkFileMapper homeworkFileMapper) {
        this.userToLessonInternalService = userToLessonInternalService;
        this.homeworkFileMapper = homeworkFileMapper;
    }

    public HomeworkFileDto upload(Long lessonId, String login, MultipartFile file) {
        var entity = userToLessonInternalService.upload(lessonId, login, file);
        return homeworkFileMapper.toDto(entity);
    }

    public FileDto download(Long lessonId, String login) {
        return userToLessonInternalService.download(lessonId, login);
    }

    public void delete(Long lessonId, String login) {
        userToLessonInternalService.delete(lessonId, login);
    }
}
