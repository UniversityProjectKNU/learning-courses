package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.model.HomeworkFile;
import com.nazar.grynko.learningcourses.model.UserToLesson;
import com.nazar.grynko.learningcourses.repository.UserToLessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserToLessonInternalService {

    private final UserToLessonRepository userToLessonRepository;
    private final HomeworkInternalService homeworkInternalService;

    public UserToLessonInternalService(UserToLessonRepository userToLessonRepository, HomeworkInternalService homeworkInternalService) {
        this.userToLessonRepository = userToLessonRepository;
        this.homeworkInternalService = homeworkInternalService;
    }

    public UserToLesson save(UserToLesson entity) {
        return userToLessonRepository.save(entity);
    }

    public Optional<UserToLesson> find(String login, Long lessonId) {
        return userToLessonRepository.findByUserLoginAndLessonId(login, lessonId);
    }

    public HomeworkFile upload(Long lessonId, String login, MultipartFile file) {
        var userToLesson = find(login, lessonId)
                .orElseThrow(IllegalArgumentException::new);

        return homeworkInternalService.upload(file, userToLesson);
    }

    public FileDto download(Long lessonId, String login) {
        var userToLesson = find(login, lessonId)
                .orElseThrow(IllegalArgumentException::new);

        return homeworkInternalService.download(userToLesson);
    }

    public void delete(Long lessonId, String login) {
        var userToLesson = find(login, lessonId)
                .orElseThrow(IllegalArgumentException::new);

        homeworkInternalService.delete(userToLesson);
    }

}
