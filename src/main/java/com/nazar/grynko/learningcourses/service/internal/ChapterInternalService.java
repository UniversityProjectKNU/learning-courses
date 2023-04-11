package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.mapper.ChapterMapper;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.property.ChapterProperties;
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
    private final ChapterProperties chapterProperties;
    private final ChapterMapper chapterMapper;

    @Autowired
    public ChapterInternalService(ChapterRepository chapterRepository, ChapterTemplateInternalService chapterTemplateInternalService,
                                  LessonInternalService lessonInternalService, ChapterProperties chapterProperties, ChapterMapper chapterMapper) {
        this.chapterRepository = chapterRepository;
        this.chapterTemplateInternalService = chapterTemplateInternalService;
        this.lessonInternalService = lessonInternalService;
        this.chapterProperties = chapterProperties;
        this.chapterMapper = chapterMapper;
    }

    public Optional<Chapter> get(Long id) {
        return chapterRepository.findById(id);
    }

    public Collection<Chapter> getAllInCourse(Long courseId) {
        return chapterRepository.findAllByCourseId(courseId);
    }

    public void delete(Long id) {
        Chapter entity = get(id).orElseThrow(IllegalArgumentException::new);
        chapterRepository.delete(entity);
    }

    public Chapter save(Chapter entity) {
        return chapterRepository.save(entity);
    }

    public List<Chapter> create(Long courseTemplateId, Course course) {
        List<ChapterTemplate> chapterTemplates = chapterTemplateInternalService
                .getAllInCourseTemplate(courseTemplateId);

        List<Chapter> entities = new ArrayList<>();
        for (ChapterTemplate template : chapterTemplates) {
            Chapter entity = create(template, course);
            entities.add(entity);
        }

        return entities;
    }

    public Chapter create(ChapterTemplate template, Course course) {
        Chapter entity = chapterMapper.fromTemplate(template).setId(null);

        entity.setCourse(course);
        entity.setIsFinished(false);

        entity = chapterRepository.save(entity);

        lessonInternalService.create(template.getId(), entity);

        return entity;
    }

    public Chapter update(Chapter entity) {
        Chapter dbChapter = chapterRepository.findById(entity.getId())
                .orElseThrow(IllegalArgumentException::new);
        setNullFields(dbChapter, entity);
        return chapterRepository.save(entity);
    }

    private void setNullFields(Chapter source, Chapter destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getTitle() == null) destination.setTitle(source.getTitle());
        if (destination.getDescription() == null) destination.setDescription(source.getDescription());
        if (destination.getNumber() == null) destination.setNumber(source.getNumber());
        if (destination.getIsFinished() == null) destination.setIsFinished(source.getIsFinished());
        if (destination.getFinalFeedback() == null) destination.setFinalFeedback(source.getFinalFeedback());
        if (destination.getCourse() == null) destination.setCourse(source.getCourse());
    }

    public boolean hasWithCourse(Long chapterId, Long courseId) {
        Optional<Chapter> optional = chapterRepository
                .getChapterByIdAndCourseId(chapterId, courseId);

        return optional.isPresent();
    }
}