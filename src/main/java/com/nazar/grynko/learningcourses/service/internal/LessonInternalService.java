package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.mapper.LessonMapper;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.Lesson;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.property.LessonProperties;
import com.nazar.grynko.learningcourses.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LessonInternalService {

    private final LessonRepository lessonRepository;
    private final LessonTemplateInternalService lessonTemplateInternalService;
    private final LessonProperties lessonProperties;
    private final LessonMapper lessonMapper;

    public LessonInternalService(LessonRepository lessonRepository, LessonTemplateInternalService lessonTemplateInternalService,
                                 LessonProperties lessonProperties, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonTemplateInternalService = lessonTemplateInternalService;
        this.lessonProperties = lessonProperties;
        this.lessonMapper = lessonMapper;
    }

    public Optional<Lesson> get(Long id) {
        return lessonRepository.findById(id);
    }

    public List<Lesson> getAllInChapter(Long chapterId) {
        return lessonRepository.getAllByChapterId(chapterId);
    }

    public void delete(Long id) {
        Lesson entity = get(id).orElseThrow(IllegalArgumentException::new);
        lessonRepository.delete(entity);
    }

    public Lesson save(Lesson entity) {
        defaultSetup(entity);
        return lessonRepository.save(entity);
    }

    public List<Lesson> create(Long chapterTemplateId, Chapter chapter) {
        List<LessonTemplate> lessonTemplates = lessonTemplateInternalService
                .getAllInChapterTemplate(chapterTemplateId);

        List<Lesson> entities = new ArrayList<>();
        for (LessonTemplate template : lessonTemplates) {
            Lesson entity = create(template, chapter);
            entities.add(entity);
        }

        return entities;
    }

    public Lesson create(LessonTemplate template, Chapter chapter) {
        Lesson entity = lessonMapper.fromTemplate(template).setId(null);
        entity.setChapter(chapter);

        defaultSetup(entity);

        return lessonRepository.save(entity);
    }

    public Lesson update(Lesson entity) {
        Lesson dbLesson = lessonRepository.findById(entity.getId())
                .orElseThrow(IllegalArgumentException::new);
        setNullFields(dbLesson, entity);
        return lessonRepository.save(entity);
    }

    private void defaultSetup(Lesson lesson) {
        if (lesson.getMaxMark() == null)
            lesson.setMaxMark(lessonProperties.getDefaultMaxMark());
        if (lesson.getSuccessMark() == null)
            lesson.setSuccessMark(lessonProperties.getDefaultSuccessMark());
        if (lesson.getIsFinished() == null)
            lesson.setIsFinished(lessonProperties.getDefaultIsFinished());
    }

    private void setNullFields(Lesson source, Lesson destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getTitle() == null) destination.setTitle(source.getTitle());
        if (destination.getDescription() == null) destination.setDescription(source.getDescription());
        if (destination.getNumber() == null) destination.setNumber(source.getNumber());
        if (destination.getIsFinished() == null) destination.setIsFinished(source.getIsFinished());
        if (destination.getMaxMark() == null) destination.setMaxMark(source.getMaxMark());
        if (destination.getSuccessMark() == null) destination.setSuccessMark(source.getSuccessMark());
        if (destination.getChapter() == null) destination.setChapter(source.getChapter());
    }

    public boolean hasWithChapter(Long id, Long chapterId, Long courseId) {
        Optional<Lesson> optional = lessonRepository
                .getLessonByIdAndChapterIdAndChapterCourseId(id, chapterId, courseId);

        return optional.isPresent();
    }
}
