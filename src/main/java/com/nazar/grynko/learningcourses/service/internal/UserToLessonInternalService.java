package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.FileDto;
import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.model.HomeworkFile;
import com.nazar.grynko.learningcourses.model.Lesson;
import com.nazar.grynko.learningcourses.model.UserToLesson;
import com.nazar.grynko.learningcourses.repository.LessonRepository;
import com.nazar.grynko.learningcourses.repository.UserToLessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserToLessonInternalService {

    private final UserToLessonRepository userToLessonRepository;
    private final LessonRepository lessonRepository;
    private final HomeworkInternalService homeworkInternalService;

    private static final String USER_LESSON_WITH_LESSON_MISSING_PATTERS = "User %s with lesson %d doesn't exist";
    private static final String USER_ID_WITH_LESSON_MISSING_PATTERS = "User %d with lesson %d doesn't exist";

    public UserToLessonInternalService(UserToLessonRepository userToLessonRepository,
                                       LessonRepository lessonRepository,
                                       HomeworkInternalService homeworkInternalService) {
        this.userToLessonRepository = userToLessonRepository;
        this.lessonRepository = lessonRepository;
        this.homeworkInternalService = homeworkInternalService;
    }

    public UserToLesson save(UserToLesson entity) {
        return userToLessonRepository.save(entity);
    }

    public UserToLesson get(String login, Long lessonId) {
        return userToLessonRepository.findByUserLoginAndLessonId(login, lessonId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_LESSON_WITH_LESSON_MISSING_PATTERS, login, lessonId)));
    }

    public UserToLesson get(Long userId, Long lessonId) {
        return userToLessonRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_ID_WITH_LESSON_MISSING_PATTERS, userId, lessonId)));
    }

    public HomeworkFile getFileInfo(Long lessonId, Long studentId) {
        var optional = userToLessonRepository.findByUserIdAndLessonId(studentId, lessonId);
        if (optional.isEmpty()) {
            return null;
        }

        var userToLesson = optional.get();
        return homeworkInternalService.getFile(userToLesson);
    }

    public HomeworkFile uploadFile(Long lessonId, String login, MultipartFile file) {
        var lesson = lessonRepository.findById(lessonId).orElseThrow(EntityNotFoundException::new); //todo refactor these methods
        if (lesson.getIsFinished()) {
            throw new IllegalStateException("You cannot upload file in finished lesson");
        }

        var userToLesson = get(login, lessonId);
        return homeworkInternalService.uploadFile(file, userToLesson);
    }

    public FileDto downloadFile(Long lessonId, Long userId) {
        var userToLesson = get(userId, lessonId);
        return homeworkInternalService.downloadFile(userToLesson);
    }

    public void deleteFile(Long lessonId, String login) {
        var userToLesson = get(login, lessonId);

        homeworkInternalService.deleteFile(userToLesson);
    }

    public void deleteFile(Long lessonId, Long userId) {
        var lesson = lessonRepository.findById(lessonId).orElseThrow(EntityNotFoundException::new); //todo refactor these methods
        if (lesson.getIsFinished()) {
            throw new IllegalStateException("You cannot delete file in finished lesson");
        }

        var userToLesson = get(userId, lessonId);

        homeworkInternalService.deleteFile(userToLesson);
    }

    public List<Lesson> getAllLessonsByUserLoginAndCourseId(String login, Long courseId) {
        return userToLessonRepository.getAllByUserLoginAndCourseId(login, courseId)
                .stream()
                .map(UserToLesson::getLesson)
                .collect(Collectors.toList());
    }

    public UserToLesson update(Long userId, Long lessonId, UserToLesson entity) {
        var dbEntity = get(userId, lessonId);

        fillNullFields(dbEntity, entity);
        entity.setId(dbEntity.getId());

        return userToLessonRepository.save(entity);
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

    private void fillNullFields(UserToLesson source, UserToLesson destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getUser() == null) destination.setUser(source.getUser());
        if (destination.getLesson() == null) destination.setLesson(source.getLesson());
        if (destination.getIsPassed() == null) destination.setIsPassed(source.getIsPassed());
        if (destination.getMark() == null) destination.setMark(source.getMark());
    }
}
