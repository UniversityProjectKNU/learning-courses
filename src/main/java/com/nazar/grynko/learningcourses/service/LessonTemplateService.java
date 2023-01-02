package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.repository.LessonTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<LessonTemplate> getAll() {
        return lessonTemplateRepository.findAll();
    }

    public void delete(Long id) {
        lessonTemplateRepository.deleteById(id);
    }

    public LessonTemplate save(LessonTemplate lessonTemplate) {
        return lessonTemplateRepository.save(lessonTemplate);
    }

    public void update(LessonTemplate lessonTemplate) {
        lessonTemplateRepository.update(lessonTemplate.getId(), lessonTemplate.getNumber(), lessonTemplate.getTitle(),
                lessonTemplate.getDescription(), lessonTemplate.getChapterTemplate().getId());
    }

}
