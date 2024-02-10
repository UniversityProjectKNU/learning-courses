package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.mapper.LessonMapper;
import com.nazar.grynko.learningcourses.model.*;
import com.nazar.grynko.learningcourses.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonInternalService {

    private final LessonRepository lessonRepository;
    private final LessonTemplateInternalService lessonTemplateInternalService;
    private final LessonMapper lessonMapper;
    private final UserToLessonInternalService userToLessonInternalService;

    private static final String LESSON_MISSING_PATTERN = "Lesson %d doesn't exist";

    public LessonInternalService(LessonRepository lessonRepository,
                                 LessonTemplateInternalService lessonTemplateInternalService,
                                 LessonMapper lessonMapper,
                                 UserToLessonInternalService userToLessonInternalService) {
        this.lessonRepository = lessonRepository;
        this.lessonTemplateInternalService = lessonTemplateInternalService;
        this.lessonMapper = lessonMapper;
        this.userToLessonInternalService = userToLessonInternalService;
    }

    public Lesson get(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LESSON_MISSING_PATTERN, lessonId)));
    }

    public List<Lesson> getAllInChapter(Long chapterId) {
        return lessonRepository.getAllByChapterId(chapterId);
    }

    public void delete(Long id) {
        var entity = get(id);
        lessonRepository.delete(entity);
    }

    public Lesson save(Lesson entity) {
        return lessonRepository.save(entity);
    }

    public List<Lesson> create(Long chapterTemplateId, Chapter chapter) {
        var lessonTemplates = lessonTemplateInternalService
                .getAllInChapterTemplate(chapterTemplateId);

        var entities = new ArrayList<Lesson>();
        for (var template : lessonTemplates) {
            entities.add(create(template, chapter));
        }

        return entities;
    }

    public void finishInChapter(Long chapterId) {
        var lessons = getAllInChapter(chapterId);

        for (var lesson : lessons) {
            lesson.setIsFinished(true);
            lessonRepository.save(lesson);
        }
    }

    public Lesson finish(Long lessonId) {
        var lesson = get(lessonId);
        if (lesson.getIsFinished()) {
            throw new IllegalStateException("Lesson is already finished");
        }

        lesson.setIsFinished(true);
        return lessonRepository.save(lesson);
    }

    public Lesson create(LessonTemplate template, Chapter chapter) {
        var entity = lessonMapper.fromTemplate(template)
                .setId(null)
                .setChapter(chapter)
                .setIsFinished(false);

        return lessonRepository.save(entity);
    }

    public Lesson update(Lesson entity) {
        var dbLesson = get(entity.getId());
        fillNullFields(dbLesson, entity);

        return lessonRepository.save(entity);
    }

    public void throwIfMissingLesson(Long lessonId) {
        lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LESSON_MISSING_PATTERN, lessonId)));
    }

    private void fillNullFields(Lesson source, Lesson destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getTitle() == null) destination.setTitle(source.getTitle());
        if (destination.getDescription() == null) destination.setDescription(source.getDescription());
        if (destination.getNumber() == null) destination.setNumber(source.getNumber());
        if (destination.getIsFinished() == null) destination.setIsFinished(source.getIsFinished());
        if (destination.getMaxMark() == null) destination.setMaxMark(source.getMaxMark());
        if (destination.getSuccessMark() == null) destination.setSuccessMark(source.getSuccessMark());
        if (destination.getChapter() == null) destination.setChapter(source.getChapter());
    }

    public List<Lesson> getAllInCourse(Long courseId) {
        return lessonRepository.getAllInCourse(courseId);
    }

    public List<UserToLesson> enroll(User user, Long courseId) {
        var lessons = getAllInCourse(courseId);

        var entities = new ArrayList<UserToLesson>();
        for (var lesson : lessons) {
            entities.add(enroll(user, lesson));
        }

        return entities;
    }

    public UserToLesson enroll(User user, Lesson lesson) {
        var entity = new UserToLesson()
                .setUser(user)
                .setLesson(lesson)
                .setMark(0)
                .setIsPassed(false);

        return userToLessonInternalService.save(entity);
    }

}
