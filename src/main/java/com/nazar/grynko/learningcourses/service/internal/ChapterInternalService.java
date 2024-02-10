package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.mapper.ChapterMapper;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ChapterInternalService {

    private final ChapterRepository chapterRepository;
    private final ChapterTemplateInternalService chapterTemplateInternalService;
    private final LessonInternalService lessonInternalService;
    private final ChapterMapper chapterMapper;

    private static final String CHAPTER_MISSING_PATTERN = "Chapter %d doesn't exist";

    @Autowired
    public ChapterInternalService(ChapterRepository chapterRepository,
                                  ChapterTemplateInternalService chapterTemplateInternalService,
                                  LessonInternalService lessonInternalService,
                                  ChapterMapper chapterMapper) {
        this.chapterRepository = chapterRepository;
        this.chapterTemplateInternalService = chapterTemplateInternalService;
        this.lessonInternalService = lessonInternalService;
        this.chapterMapper = chapterMapper;
    }

    public Chapter get(Long chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CHAPTER_MISSING_PATTERN, chapterId)));
    }

    public Collection<Chapter> getAllInCourse(Long courseId) {
        return chapterRepository.findAllByCourseId(courseId);
    }

    public void delete(Long chapterId) {
        var entity = get(chapterId);
        chapterRepository.delete(entity);
    }

    public Chapter save(Chapter entity) {
        return chapterRepository.save(entity);
    }

    public List<Chapter> create(Long courseTemplateId, Course course) {
        var chapterTemplates = chapterTemplateInternalService
                .getAllInCourseTemplate(courseTemplateId);

        var entities = new ArrayList<Chapter>();
        for (var template : chapterTemplates) {
            var entity = create(template, course);
            entities.add(entity);
        }

        return entities;
    }

    public Chapter create(ChapterTemplate template, Course course) {
        var entity = chapterMapper.fromTemplate(template)
                .setId(null)
                .setCourse(course)
                .setIsFinished(false);

        entity = chapterRepository.save(entity);

        lessonInternalService.create(template.getId(), entity);

        return entity;
    }

    public void finish(Long courseId) {
        var chapters = getAllInCourse(courseId);

        for (var chapter : chapters) {
            finish(chapter);
        }
    }

    private void finish(Chapter chapter) {
        chapter.setIsFinished(true);
        chapterRepository.save(chapter);

        lessonInternalService.finishInChapter(chapter.getId());
    }

    public Chapter update(Chapter entity) {
        var dbChapter = get(entity.getId());
        fillNullFields(dbChapter, entity);

        return chapterRepository.save(entity);
    }

    public void throwIfMissingChapter(Long chapterId) {
        chapterRepository.findById(chapterId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CHAPTER_MISSING_PATTERN, chapterId)));
    }

    private void fillNullFields(Chapter source, Chapter destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getTitle() == null) destination.setTitle(source.getTitle());
        if (destination.getDescription() == null) destination.setDescription(source.getDescription());
        if (destination.getNumber() == null) destination.setNumber(source.getNumber());
        if (destination.getIsFinished() == null) destination.setIsFinished(source.getIsFinished());
        if (destination.getCourse() == null) destination.setCourse(source.getCourse());
    }
}
