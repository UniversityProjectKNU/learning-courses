package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.repository.ChapterTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterTemplateService {
    
    private final ChapterTemplateRepository chapterTemplateRepository;
    private final CourseTemplateService courseTemplateService;

    @Autowired    
    public ChapterTemplateService(ChapterTemplateRepository chapterTemplateRepository, CourseTemplateService courseTemplateService) {
        this.chapterTemplateRepository = chapterTemplateRepository;
        this.courseTemplateService = courseTemplateService;
    }

    public Optional<ChapterTemplate> get(Long id) {
        return chapterTemplateRepository.findById(id);
    }

    public void delete(Long id) {
        ChapterTemplate entity = chapterTemplateRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        chapterTemplateRepository.delete(entity);
    }

    public ChapterTemplate save(ChapterTemplate entity, Long courseTemplateId) {
        CourseTemplate courseTemplate = courseTemplateService.get(courseTemplateId)
                .orElseThrow(InvalidPathException::new);
        entity.setCourseTemplate(courseTemplate);

        return chapterTemplateRepository.save(entity);
    }

    public ChapterTemplate update(ChapterTemplate entity) {
        ChapterTemplate dbChapterTemplate = chapterTemplateRepository.findById(entity.getId())
                .orElseThrow(IllegalArgumentException::new);
        setNullFields(dbChapterTemplate, entity);
        return chapterTemplateRepository.save(entity);
    }

    public List<ChapterTemplate> getAllInCourseTemplate(Long courseTemplateId) {
        courseTemplateService.get(courseTemplateId).orElseThrow(IllegalArgumentException::new);
        return chapterTemplateRepository.getChapterTemplatesByCourseTemplateId(courseTemplateId);
    }

    public boolean hasWithCourseTemplate(Long chapterTemplateId, Long courseTemplateId) {
        Optional<ChapterTemplate> optional = chapterTemplateRepository
                .getChapterTemplateByIdAndCourseTemplateId(chapterTemplateId, courseTemplateId);
        return optional.isPresent();
    }

    private void setNullFields(ChapterTemplate source, ChapterTemplate destination) {
        if(destination.getId() == null) destination.setId(source.getId());
        if(destination.getTitle() == null) destination.setTitle(source.getTitle());
        if(destination.getDescription() == null) destination.setDescription(source.getDescription());
        if(destination.getNumber() == null) destination.setNumber(source.getNumber());
        if(destination.getCourseTemplate() == null) destination.setCourseTemplate(source.getCourseTemplate());
    }

}
