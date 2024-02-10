package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.repository.ChapterTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ChapterTemplateInternalService {

    private final ChapterTemplateRepository chapterTemplateRepository;
    private final CourseTemplateInternalService courseTemplateInternalService;

    private static final String CHAPTER_TEMPLATE_MISSING_PATTERN = "Chapter template %d doesn't exist";

    @Autowired
    public ChapterTemplateInternalService(ChapterTemplateRepository chapterTemplateRepository,
                                          CourseTemplateInternalService courseTemplateInternalService) {
        this.chapterTemplateRepository = chapterTemplateRepository;
        this.courseTemplateInternalService = courseTemplateInternalService;
    }

    public ChapterTemplate get(Long chapterTemplateId) {
        return chapterTemplateRepository.findById(chapterTemplateId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CHAPTER_TEMPLATE_MISSING_PATTERN, chapterTemplateId)));
    }

    public void delete(Long chapterTemplateId) {
        var entity = get(chapterTemplateId);
        chapterTemplateRepository.delete(entity);
    }

    public ChapterTemplate save(ChapterTemplate entity, Long courseTemplateId) {
        var courseTemplate = courseTemplateInternalService.get(courseTemplateId);
        entity.setCourseTemplate(courseTemplate);

        return chapterTemplateRepository.save(entity);
    }

    public ChapterTemplate update(ChapterTemplate entity) {
        var dbChapterTemplate = get(entity.getId());
        fillNullFields(dbChapterTemplate, entity);

        return chapterTemplateRepository.save(entity);
    }

    public List<ChapterTemplate> getAllInCourseTemplate(Long courseTemplateId) {
        courseTemplateInternalService.throwIfMissingCourseTemplate(courseTemplateId);
        return chapterTemplateRepository.getChapterTemplatesByCourseTemplateId(courseTemplateId);
    }

    public void throwIfMissingChapterTemplate(Long chapterTemplateId) {
        chapterTemplateRepository.findById(chapterTemplateId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CHAPTER_TEMPLATE_MISSING_PATTERN, chapterTemplateId)));
    }

    private void fillNullFields(ChapterTemplate source, ChapterTemplate destination) {
        if (isNull(destination.getId())) destination.setId(source.getId());
        if (isNull(destination.getTitle())) destination.setTitle(source.getTitle());
        if (isNull(destination.getDescription())) destination.setDescription(source.getDescription());
        if (isNull(destination.getNumber())) destination.setNumber(source.getNumber());
        if (isNull(destination.getCourseTemplate())) destination.setCourseTemplate(source.getCourseTemplate());
    }

}
