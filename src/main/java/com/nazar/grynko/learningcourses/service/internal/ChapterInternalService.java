package com.nazar.grynko.learningcourses.service.internal;

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
import java.util.Optional;

@Service
public class ChapterInternalService {

    private final ChapterRepository chapterRepository;
    private final ChapterTemplateInternalService chapterTemplateInternalService;
    private final LessonInternalService lessonInternalService;
    private final ChapterMapper chapterMapper;

    @Autowired
    public ChapterInternalService(ChapterRepository chapterRepository, ChapterTemplateInternalService chapterTemplateInternalService,
                                  LessonInternalService lessonInternalService, ChapterMapper chapterMapper) {
        this.chapterRepository = chapterRepository;
        this.chapterTemplateInternalService = chapterTemplateInternalService;
        this.lessonInternalService = lessonInternalService;
        this.chapterMapper = chapterMapper;
    }

    public Optional<Chapter> get(Long id) {
        return chapterRepository.findById(id);
    }

    public Collection<Chapter> getAllInCourse(Long courseId) {
        return chapterRepository.findAllByCourseId(courseId);
    }

    public void delete(Long id) {
        var entity = get(id).orElseThrow(IllegalArgumentException::new);
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
        var entity = chapterMapper.fromTemplate(template).setId(null);

        entity.setCourse(course);
        entity.setIsFinished(false);

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
        var dbChapter = chapterRepository.findById(entity.getId())
                .orElseThrow(IllegalArgumentException::new);
        fillNullFields(dbChapter, entity);
        return chapterRepository.save(entity);
    }

    private void fillNullFields(Chapter source, Chapter destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getTitle() == null) destination.setTitle(source.getTitle());
        if (destination.getDescription() == null) destination.setDescription(source.getDescription());
        if (destination.getNumber() == null) destination.setNumber(source.getNumber());
        if (destination.getIsFinished() == null) destination.setIsFinished(source.getIsFinished());
        if (destination.getCourse() == null) destination.setCourse(source.getCourse());
    }

    public boolean hasWithCourse(Long chapterId, Long courseId) {
        var optional = chapterRepository
                .getChapterByIdAndCourseId(chapterId, courseId);

        return optional.isPresent();
    }
}
