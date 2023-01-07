package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.repository.ChapterTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ChapterTemplateService {
    
    private final ChapterTemplateRepository chapterTemplateRepository;

    @Autowired    
    public ChapterTemplateService(ChapterTemplateRepository chapterTemplateRepository) {
        this.chapterTemplateRepository = chapterTemplateRepository;
    }

    public Optional<ChapterTemplate> get(Long id) {
        return chapterTemplateRepository.findById(id);
    }

    public void delete(Long id) {
        ChapterTemplate chapterTemplate = chapterTemplateRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        chapterTemplateRepository.delete(chapterTemplate);
    }

    public ChapterTemplate save(ChapterTemplate chapterTemplate) {
        return chapterTemplateRepository.save(chapterTemplate);
    }

    public ChapterTemplate update(ChapterTemplate chapterTemplate) {
        ChapterTemplate dbChapterTemplate = chapterTemplateRepository.findById(chapterTemplate.getId())
                .orElseThrow(IllegalArgumentException::new);
        setNullFields(dbChapterTemplate, chapterTemplate);
        return chapterTemplateRepository.save(dbChapterTemplate);
    }

    public Set<LessonTemplate> getAllLessonsInChapterTemplate(Long chapterTemplateId) {
        get(chapterTemplateId).orElseThrow(IllegalArgumentException::new);
        return chapterTemplateRepository.getAllLessonsInChapterTemplate(chapterTemplateId);
    }

    public void validateAndSetId(ChapterTemplate chapterTemplate, Long id) {
        if(chapterTemplate.getId() != null && !chapterTemplate.getId().equals(id))
            throw new IllegalStateException();
        chapterTemplate.setId(id);
    }

    public void setNullFields(ChapterTemplate source, ChapterTemplate destination) {
        if(destination.getId() == null) destination.setId(source.getId());
        if(destination.getTitle() == null) destination.setTitle(source.getTitle());
        if(destination.getDescription() == null) destination.setDescription(source.getDescription());
        if(destination.getNumber() == null) destination.setNumber(source.getNumber());
        if(destination.getCourseTemplate() == null) destination.setCourseTemplate(source.getCourseTemplate());
    }

}
