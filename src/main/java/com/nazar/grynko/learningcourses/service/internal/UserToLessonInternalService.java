package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.model.HomeworkFile;
import com.nazar.grynko.learningcourses.model.Lesson;
import com.nazar.grynko.learningcourses.model.UserToLesson;
import com.nazar.grynko.learningcourses.repository.UserToLessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserToLessonInternalService {

    private final UserToLessonRepository userToLessonRepository;
    private final HomeworkInternalService homeworkInternalService;

    public UserToLessonInternalService(UserToLessonRepository userToLessonRepository,
                                       HomeworkInternalService homeworkInternalService) {
        this.userToLessonRepository = userToLessonRepository;
        this.homeworkInternalService = homeworkInternalService;
    }

    public UserToLesson save(UserToLesson entity) {
        return userToLessonRepository.save(entity);
    }

    public Optional<UserToLesson> get(String login, Long lessonId) {
        return userToLessonRepository.findByUserLoginAndLessonId(login, lessonId);
    }

    public Optional<UserToLesson> get(Long userId, Long lessonId) {
        return userToLessonRepository.findByUserIdAndLessonId(userId, lessonId);
    }

    public HomeworkFile getFileInfo(Long lessonId, String login) {
        var userToLesson = get(login, lessonId)
                .orElseThrow(IllegalArgumentException::new);

        return homeworkInternalService.get(userToLesson);
    }

    public HomeworkFile getFileInfo(Long lessonId, Long studentId) {
        var optional = get(studentId, lessonId);
        if (optional.isEmpty()) {
            return null;
        }
        var userToLesson = optional.get();

        return homeworkInternalService.get(userToLesson);
    }

    public HomeworkFile upload(Long lessonId, String login, MultipartFile file) {
        var userToLesson = get(login, lessonId)
                .orElseThrow(IllegalArgumentException::new);

        return homeworkInternalService.uploadHomework(file, userToLesson);
    }

    public FileDto download(Long lessonId, Long userId) {
        var userToLesson = get(userId, lessonId)
                .orElseThrow(IllegalArgumentException::new);

        return homeworkInternalService.download(userToLesson);
    }

    public void delete(Long lessonId, String login) {
        var userToLesson = get(login, lessonId)
                .orElseThrow(IllegalArgumentException::new);

        homeworkInternalService.delete(userToLesson);
    }

    public void delete(Long lessonId, Long userId) {
        var userToLesson = get(userId, lessonId)
                .orElseThrow(IllegalArgumentException::new);

        homeworkInternalService.delete(userToLesson);
    }

    public List<Lesson> getAllLessonsByUserLoginAndCourseId(String login, Long courseId) {
        return userToLessonRepository.getAllByUserLoginAndCourseId(login, courseId)
                .stream()
                .map(UserToLesson::getLesson)
                .collect(Collectors.toList());
    }

    public UserToLesson update(Long userId, Long lessonId, UserToLesson entity) {
        var dbEntity = userToLessonRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseThrow(IllegalArgumentException::new);
        fillNullFields(dbEntity, entity);
        entity.setId(dbEntity.getId());
        return userToLessonRepository.save(entity);
    }

    private void fillNullFields(UserToLesson source, UserToLesson destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getUser() == null) destination.setUser(source.getUser());
        if (destination.getLesson() == null) destination.setLesson(source.getLesson());
        if (destination.getIsPassed() == null) destination.setIsPassed(source.getIsPassed());
        if (destination.getMark() == null) destination.setMark(source.getMark());
    }

    public void setIsPassedForLessonsInCourse(Long courseId) {
        var usersToLessons = userToLessonRepository.getAllByCourseId(courseId);
        usersToLessons.forEach(utl -> {
            var lesson = utl.getLesson();
            utl.setIsPassed(utl.getMark() >= lesson.getSuccessMark());
        });
    }

    public List<UserToLesson> getAllByUserLoginAndCourseId(String login, Long courseId) {
        return userToLessonRepository.getAllByUserLoginAndCourseId(login, courseId);
    }

    public List<UserToLesson> getAllInChapterByChapterIdAndLogin(Long chapterId, String login) {
        return userToLessonRepository.getAllInChapterByChapterIdAndLogin(chapterId, login);
    }

    public List<UserToLesson> getAllInChapterByChapterId(Long chapterId) {
        return userToLessonRepository.getAllInChapterByChapterId(chapterId);
    }

    public List<UserToLesson> getAllUserToLessonInfoForLesson(Long lessonId) {
        return userToLessonRepository.getAllByLessonId(lessonId);
    }
}
