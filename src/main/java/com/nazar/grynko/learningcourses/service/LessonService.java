package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoSave;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertolesson.UserToLessonDto;
import com.nazar.grynko.learningcourses.mapper.LessonMapper;
import com.nazar.grynko.learningcourses.mapper.UserToLessonMapper;
import com.nazar.grynko.learningcourses.service.internal.ChapterInternalService;
import com.nazar.grynko.learningcourses.service.internal.CourseInternalService;
import com.nazar.grynko.learningcourses.service.internal.LessonInternalService;
import com.nazar.grynko.learningcourses.service.internal.UserToLessonInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LessonService {

    private final LessonInternalService lessonInternalService;
    private final ChapterInternalService chapterInternalService;
    private final CourseInternalService courseInternalService;
    private final UserToLessonInternalService userToLessonInternalService;
    private final LessonMapper lessonMapper;
    private final UserToLessonMapper userToLessonMapper;

    @Autowired
    public LessonService(LessonInternalService lessonInternalService,
                         ChapterInternalService chapterInternalService,
                         CourseInternalService courseInternalService,
                         UserToLessonInternalService userToLessonInternalService,
                         LessonMapper lessonMapper,
                         UserToLessonMapper userToLessonMapper) {
        this.lessonInternalService = lessonInternalService;
        this.chapterInternalService = chapterInternalService;
        this.courseInternalService = courseInternalService;
        this.userToLessonInternalService = userToLessonInternalService;
        this.lessonMapper = lessonMapper;
        this.userToLessonMapper = userToLessonMapper;
    }

    public Optional<LessonDto> get(Long id) {
        return lessonInternalService.get(id)
                .flatMap(val -> Optional.of(lessonMapper.toDto(val)));
    }

    public List<LessonDto> getAllInChapter(Long chapterId) {
        chapterInternalService.get(chapterId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Chapter %d doesn't exist", chapterId)));

        return lessonInternalService.getAllInChapter(chapterId)
                .stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getAllInCourse(Long courseId) {
        courseInternalService.get(courseId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Chapter %d doesn't exist", courseId)));

        return lessonInternalService.getAllInCourse(courseId)
                .stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getUsersLessonsForCourse(Long courseId, String login) {
        var lessons = lessonInternalService.getUsersLessonsForCourse(courseId, login);
        return lessons.stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserToLessonDto getUsersLessonInfo(Long id, String login) {
        var userToLesson = userToLessonInternalService.get(login, id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User doesn't have lesson %d", id)));
        return userToLessonMapper.toDto(userToLesson);
    }

    public void delete(Long id) {
        lessonInternalService.delete(id);
    }

    public LessonDto save(LessonDtoSave dto, Long chapterId) {
        var entity = lessonMapper.fromDtoSave(dto);

        var chapter = chapterInternalService.get(chapterId)
                .orElseThrow(IllegalArgumentException::new);
        entity.setChapter(chapter);
        entity.setIsFinished(false);

        entity = lessonInternalService.save(entity);
        return lessonMapper.toDto(entity);
    }

    public LessonDto update(LessonDtoUpdate dto, Long id) {
        var entity = lessonMapper.fromDtoUpdate(dto).setId(id);
        entity = lessonInternalService.update(entity);
        return lessonMapper.toDto(entity);
    }

    public boolean hasWithChapter(Long id, Long chapterId, Long courseId) {
        return lessonInternalService.hasWithChapter(id, chapterId, courseId);
    }

    public boolean hasWithCourse(Long id, Long courseId) {
        return lessonInternalService.hasWithCourse(id, courseId);
    }

    public UserToLessonDto updateUserToLesson(Long id, Long userId, UserToCourseDtoUpdate dto) {
        var entity = userToLessonMapper.fromDtoUpdate(dto).setId(id);
        entity = userToLessonInternalService.update(userId, id, entity);
        return userToLessonMapper.toDto(entity);
    }
}
