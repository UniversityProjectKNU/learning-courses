package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.repository.ChapterTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterTemplateInternalService {

    private final ChapterTemplateRepository chapterTemplateRepository;
    private final CourseTemplateInternalService courseTemplateInternalService;

    @Autowired
    public ChapterTemplateInternalService(ChapterTemplateRepository chapterTemplateRepository, CourseTemplateInternalService courseTemplateInternalService) {
        this.chapterTemplateRepository = chapterTemplateRepository;
        this.courseTemplateInternalService = courseTemplateInternalService;
    }

    public Optional<ChapterTemplate> get(Long id) {
        return chapterTemplateRepository.findById(id);
    }

    public void delete(Long id) {
        var entity = chapterTemplateRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        chapterTemplateRepository.delete(entity);
    }

    public ChapterTemplate save(ChapterTemplate entity, Long courseTemplateId) {
        var courseTemplate = courseTemplateInternalService.get(courseTemplateId)
                .orElseThrow(InvalidPathException::new);
        entity.setCourseTemplate(courseTemplate);

        return chapterTemplateRepository.save(entity);
    }

    public ChapterTemplate update(ChapterTemplate entity) {
        var dbChapterTemplate = chapterTemplateRepository.findById(entity.getId())
                .orElseThrow(IllegalArgumentException::new);
        fillNullFields(dbChapterTemplate, entity);
        return chapterTemplateRepository.save(entity);
    }

    public List<ChapterTemplate> getAllInCourseTemplate(Long courseTemplateId) {
        return chapterTemplateRepository.getChapterTemplatesByCourseTemplateId(courseTemplateId);
    }

    public boolean hasWithCourseTemplate(Long chapterTemplateId, Long courseTemplateId) {
        var optional = chapterTemplateRepository
                .getChapterTemplateByIdAndCourseTemplateId(chapterTemplateId, courseTemplateId);
        return optional.isPresent();
    }

    private void fillNullFields(ChapterTemplate source, ChapterTemplate destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getTitle() == null) destination.setTitle(source.getTitle());
        if (destination.getDescription() == null) destination.setDescription(source.getDescription());
        if (destination.getNumber() == null) destination.setNumber(source.getNumber());
        if (destination.getCourseTemplate() == null) destination.setCourseTemplate(source.getCourseTemplate());
    }

}
