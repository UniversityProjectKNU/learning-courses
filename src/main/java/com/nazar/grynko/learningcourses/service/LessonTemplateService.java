package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.repository.LessonTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class LessonTemplateService {

    private final LessonTemplateRepository lessonTemplateRepository;
    private final ChapterTemplateService chapterTemplateService;

    @Autowired
    public LessonTemplateService(LessonTemplateRepository lessonTemplateRepository, ChapterTemplateService chapterTemplateService) {
        this.lessonTemplateRepository = lessonTemplateRepository;
        this.chapterTemplateService = chapterTemplateService;
    }

    public Optional<LessonTemplate> get(Long id) {
        return lessonTemplateRepository.findById(id);
    }

    public void delete(Long id) {
        lessonTemplateRepository.deleteById(id);
    }

    public LessonTemplate save(LessonTemplate lessonTemplate) {
        return lessonTemplateRepository.save(lessonTemplate);
    }

    public LessonTemplate update(LessonTemplate lessonTemplate) {
        LessonTemplate dbLessonTemplate = lessonTemplateRepository.findById(lessonTemplate.getId())
                .orElseThrow(IllegalArgumentException::new);
        setNullFields(dbLessonTemplate, lessonTemplate);
        return lessonTemplateRepository.save(lessonTemplate);
    }

    public Set<LessonTemplate> getAllInChapterTemplate(Long chapterTemplateId) {
        chapterTemplateService.get(chapterTemplateId)
                .orElseThrow(IllegalArgumentException::new);
        return lessonTemplateRepository.getLessonTemplatesByChapterTemplateId(chapterTemplateId);
    }

    private void setNullFields(LessonTemplate source, LessonTemplate destination) {
        if(destination.getId() == null) destination.setId(source.getId());
        if(destination.getTitle() == null) destination.setTitle(source.getTitle());
        if(destination.getDescription() == null) destination.setDescription(source.getDescription());
        if(destination.getNumber() == null) destination.setNumber(source.getNumber());
        if(destination.getChapterTemplate() == null) destination.setChapterTemplate(source.getChapterTemplate());
    }

}
