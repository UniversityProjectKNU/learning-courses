package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.repository.LessonTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LessonTemplateService {

    private final LessonTemplateRepository lessonTemplateRepository;

    @Autowired
    public LessonTemplateService(LessonTemplateRepository lessonTemplateRepository) {
        this.lessonTemplateRepository = lessonTemplateRepository;
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

    public void validateAndSetId(LessonTemplate lessonTemplate, Long id) {
        if(lessonTemplate.getId() != null && !lessonTemplate.getId().equals(id))
            throw new IllegalStateException();
        lessonTemplate.setId(id);
    }

    public void setNullFields(LessonTemplate source, LessonTemplate destination) {
        if(destination.getId() == null) destination.setId(source.getId());
        if(destination.getTitle() == null) destination.setTitle(source.getTitle());
        if(destination.getDescription() == null) destination.setDescription(source.getDescription());
        if(destination.getNumber() == null) destination.setNumber(source.getNumber());
        if(destination.getChapterTemplate() == null) destination.setChapterTemplate(source.getChapterTemplate());
    }

}
