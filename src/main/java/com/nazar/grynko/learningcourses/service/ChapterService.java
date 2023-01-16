package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.property.ChapterProperties;
import com.nazar.grynko.learningcourses.repository.ChapterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final ChapterTemplateService chapterTemplateService;
    private final LessonService lessonService;
    private final ChapterProperties chapterProperties;
    private final ModelMapper modelMapper;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository, ChapterTemplateService chapterTemplateService,
                          LessonService lessonService, ChapterProperties chapterProperties, ModelMapper modelMapper) {
        this.chapterRepository = chapterRepository;
        this.chapterTemplateService = chapterTemplateService;
        this.lessonService = lessonService;
        this.chapterProperties = chapterProperties;
        this.modelMapper = modelMapper;
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
        defaultSetup(entity);
        return chapterRepository.save(entity);
    }

    public List<Chapter> create(Long courseTemplateId, Course course) {
        Set<ChapterTemplate> chapterTemplates = chapterTemplateService
                .getAllInCourseTemplate(courseTemplateId);

        List<Chapter> entities = new ArrayList<>();
        for(ChapterTemplate template: chapterTemplates) {
            Chapter entity = create(template, course);
            entities.add(entity);
        }

        return entities;
    }

    public Chapter create(ChapterTemplate template, Course course) {
        Chapter entity = fromTemplate(template).setId(null);
        // change
        entity.setCourse(course);
        defaultSetup(entity);

        entity = chapterRepository.save(entity);

        lessonService.create(template.getId(), entity);

        return entity;
    }

    public Chapter update(Chapter entity) {
        Chapter dbChapter = chapterRepository.findById(entity.getId())
                .orElseThrow(IllegalArgumentException::new);
        setNullFields(dbChapter, entity);
        return chapterRepository.save(entity);
    }

    private void defaultSetup(Chapter entity) {
        if(entity.getIsFinished() == null)
            entity.setIsFinished(chapterProperties.getDefaultIsFinished());
    }

    private Chapter fromTemplate(ChapterTemplate template) {
        return modelMapper.map(template, Chapter.class);
    }

    private void setNullFields(Chapter source, Chapter destination) {
        if(destination.getId() == null) destination.setId(source.getId());
        if(destination.getTitle() == null) destination.setTitle(source.getTitle());
        if(destination.getDescription() == null) destination.setDescription(source.getDescription());
        if(destination.getNumber() == null) destination.setNumber(source.getNumber());
        if(destination.getIsFinished() == null) destination.setIsFinished(source.getIsFinished());
        if(destination.getFinalFeedback() == null) destination.setFinalFeedback(source.getFinalFeedback());
        if(destination.getCourse() == null) destination.setCourse(source.getCourse());
    }

    public boolean hasWithCourse(Long chapterId, Long courseId) {
        Optional<Chapter> optional = chapterRepository
                .getChapterByIdAndCourseId(chapterId, courseId);

        return optional.isPresent();
    }
}
