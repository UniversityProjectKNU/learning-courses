package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.repository.ChapterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final ChapterTemplateService chapterTemplateService;
    private final LessonService lessonService;
    private final ModelMapper modelMapper;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository, ChapterTemplateService chapterTemplateService,
                          LessonService lessonService, ModelMapper modelMapper) {
        this.chapterRepository = chapterRepository;
        this.chapterTemplateService = chapterTemplateService;
        this.lessonService = lessonService;
        this.modelMapper = modelMapper;
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

    private Chapter create(ChapterTemplate template, Course course) {
        Chapter entity = fromTemplate(template);
        entity.setCourse(course);

        entity = chapterRepository.save(entity);

        lessonService.create(template.getId(), entity);

        return entity;
    }

    private Chapter fromTemplate(ChapterTemplate template) {
        return modelMapper.map(template, Chapter.class);
    }

}
