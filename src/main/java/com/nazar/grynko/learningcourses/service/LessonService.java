package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.Lesson;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.repository.LessonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonTemplateService lessonTemplateService;
    private final ModelMapper modelMapper;

    public LessonService(LessonRepository lessonRepository, LessonTemplateService lessonTemplateService,
                         ModelMapper modelMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonTemplateService = lessonTemplateService;
        this.modelMapper = modelMapper;
    }

    public List<Lesson> create(Long chapterTemplateId, Chapter chapter) {
        Set<LessonTemplate> lessonTemplates = lessonTemplateService
                .getAllInChapterTemplate(chapterTemplateId);

        List<Lesson> entities = new ArrayList<>();
        for(LessonTemplate template: lessonTemplates) {
            Lesson entity = create(template, chapter);
            entities.add(entity);
        }

        return entities;
    }

    private Lesson create(LessonTemplate template, Chapter chapter) {
        Lesson entity = fromTemplate(template);
        entity.setChapter(chapter);

        return lessonRepository.save(entity);
    }

    private Lesson fromTemplate(LessonTemplate template) {
        return modelMapper.map(template, Lesson.class);
    }

}
