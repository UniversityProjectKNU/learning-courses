package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.repository.ChapterTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        chapterTemplateRepository.deleteById(id);
    }

    public ChapterTemplate save(ChapterTemplate chapterTemplate) {
        return chapterTemplateRepository.save(chapterTemplate);
    }

    public void update(ChapterTemplate chapterTemplate) {
        chapterTemplateRepository.update(chapterTemplate.getId(), chapterTemplate.getNumber(), chapterTemplate.getTitle(),
                chapterTemplate.getDescription(), chapterTemplate.getCourseTemplate());
    }

}
