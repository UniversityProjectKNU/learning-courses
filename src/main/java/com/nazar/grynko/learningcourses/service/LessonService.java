package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoSave;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDtoUpdate;
import com.nazar.grynko.learningcourses.mapper.LessonMapper;
import com.nazar.grynko.learningcourses.mapper.UserToLessonMapper;
import com.nazar.grynko.learningcourses.service.internal.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LessonService {

    private final LessonInternalService lessonInternalService;
    private final UserInternalService userInternalService;
    private final ChapterInternalService chapterInternalService;
    private final CourseInternalService courseInternalService;
    private final UserToLessonInternalService userToLessonInternalService;
    private final LessonMapper lessonMapper;
    private final UserToLessonMapper userToLessonMapper;

    public LessonService(LessonInternalService lessonInternalService,
                         UserInternalService userInternalService,
                         ChapterInternalService chapterInternalService,
                         CourseInternalService courseInternalService,
                         UserToLessonInternalService userToLessonInternalService,
                         LessonMapper lessonMapper,
                         UserToLessonMapper userToLessonMapper) {
        this.lessonInternalService = lessonInternalService;
        this.userInternalService = userInternalService;
        this.chapterInternalService = chapterInternalService;
        this.courseInternalService = courseInternalService;
        this.userToLessonInternalService = userToLessonInternalService;
        this.lessonMapper = lessonMapper;
        this.userToLessonMapper = userToLessonMapper;
    }

    public LessonDto get(Long lessonId) {
        var entity = lessonInternalService.get(lessonId);
        return lessonMapper.toDto(entity);
    }

    public List<LessonDto> getAllInChapter(Long chapterId) {
        chapterInternalService.throwIfMissingChapter(chapterId);
        return lessonInternalService.getAllInChapter(chapterId)
                .stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getAllInCourse(Long courseId) {
        courseInternalService.throwIfMissingCourse(courseId);
        return lessonInternalService.getAllInCourse(courseId)
                .stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserToLessonDto> getAllUsersLessonsForCourse(Long courseId, String login) {
        courseInternalService.throwIfMissingCourse(courseId);
        userInternalService.throwIfMissingUser(login);

        return userToLessonInternalService.getAllByUserLoginAndCourseId(login, courseId)
                .stream()
                .map(userToLessonMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserToLessonDto> getAllLessonsOfOneUserForChapter(Long chapterId, String login) {
        chapterInternalService.throwIfMissingChapter(chapterId);
        userInternalService.throwIfMissingUser(login);

        return userToLessonInternalService.getAllInChapterByChapterIdAndLogin(chapterId, login)
                .stream()
                .map(userToLessonMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserToLessonDto> getAllUserToLessonInChapter(Long chapterId) {
        chapterInternalService.throwIfMissingChapter(chapterId);

        return userToLessonInternalService.getAllInChapterByChapterId(chapterId)
                .stream()
                .map(userToLessonMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserToLessonDto getUsersLessonInfoByLogin(Long lessonId, String login) {
        lessonInternalService.throwIfMissingLesson(lessonId);
        userInternalService.throwIfMissingUser(login);

        var userToLesson = userToLessonInternalService.get(login, lessonId);
        return userToLessonMapper.toDto(userToLesson);
    }

    public UserToLessonDto getUsersLessonInfo(Long lessonId, Long userId) {
        lessonInternalService.throwIfMissingLesson(lessonId);
        userInternalService.throwIfMissingUser(userId);

        var userToLesson = userToLessonInternalService.get(userId, lessonId);
        return userToLessonMapper.toDto(userToLesson);
    }

    public void delete(Long lessonId) {
        lessonInternalService.throwIfMissingLesson(lessonId);

        lessonInternalService.delete(lessonId);
    }

    public LessonDto save(LessonDtoSave dto, Long chapterId) {
        var entity = lessonMapper.fromDtoSave(dto);

        var chapter = chapterInternalService.get(chapterId);
        entity.setChapter(chapter);
        entity.setIsFinished(false);

        entity = lessonInternalService.save(entity);
        return lessonMapper.toDto(entity);
    }

    public LessonDto update(LessonDtoUpdate dto, Long lessonId) {
        lessonInternalService.throwIfMissingLesson(lessonId);

        var entity = lessonMapper.fromDtoUpdate(dto)
                .setId(lessonId);
        entity = lessonInternalService.update(entity);
        return lessonMapper.toDto(entity);
    }

    public UserToLessonDto updateUserToLesson(Long lessonId, Long userId, UserToLessonDtoUpdate dto) {
        lessonInternalService.throwIfMissingLesson(lessonId);
        userInternalService.throwIfMissingUser(userId);

        var entity = userToLessonMapper.fromDtoUpdate(dto);
        entity = userToLessonInternalService.update(userId, lessonId, entity);
        return userToLessonMapper.toDto(entity);
    }

    public LessonDto finish(Long lessonId) {
        lessonInternalService.throwIfMissingLesson(lessonId);

        var entity = lessonInternalService.finish(lessonId);
        return lessonMapper.toDto(entity);
    }

    public boolean isFinished(Long id) {
        return lessonInternalService.get(id).getIsFinished();
    }

    public List<UserToLessonDto> getAllUserToLessonInfoForLesson(Long lessonId) {
        lessonInternalService.throwIfMissingLesson(lessonId);

        return userToLessonInternalService.getAllUserToLessonInfoForLesson(lessonId)
                .stream()
                .map(userToLessonMapper::toDto)
                .collect(Collectors.toList());
    }
}
