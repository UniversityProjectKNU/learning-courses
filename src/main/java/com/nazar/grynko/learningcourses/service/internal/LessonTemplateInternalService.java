package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.repository.LessonTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class LessonTemplateInternalService {

    private final LessonTemplateRepository lessonTemplateRepository;
    private final ChapterTemplateInternalService chapterTemplateInternalService;

    private static final String LESSON_TEMPLATE_MISSING_PATTERN = "Lesson template %d doesn't exist";

    @Autowired
    public LessonTemplateInternalService(LessonTemplateRepository lessonTemplateRepository,
                                         ChapterTemplateInternalService chapterTemplateInternalService) {
        this.lessonTemplateRepository = lessonTemplateRepository;
        this.chapterTemplateInternalService = chapterTemplateInternalService;
    }

    public LessonTemplate get(Long lessonTemplateId) {
        return lessonTemplateRepository.findById(lessonTemplateId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LESSON_TEMPLATE_MISSING_PATTERN, lessonTemplateId)));
    }

    public void delete(Long lessonTemplateId) {
        lessonTemplateRepository.deleteById(lessonTemplateId);
    }

    public LessonTemplate save(LessonTemplate entity, Long chapterTemplateId) {
        ChapterTemplate chapterTemplate = chapterTemplateInternalService.get(chapterTemplateId);
        entity.setChapterTemplate(chapterTemplate);

        return lessonTemplateRepository.save(entity);
    }

    public LessonTemplate update(LessonTemplate entity) {
        LessonTemplate dbLessonTemplate = get(entity.getId());
        fillNullFields(dbLessonTemplate, entity);

        return lessonTemplateRepository.save(entity);
    }

    public List<LessonTemplate> getAllInChapterTemplate(Long chapterTemplateId) {
        return lessonTemplateRepository.getLessonTemplatesByChapterTemplateId(chapterTemplateId);
    }

    public List<LessonTemplate> getAllInCourseTemplate(Long courseTemplateId) {
        return lessonTemplateRepository.getAllInCourse(courseTemplateId);
    }

    public void throwIfMissingLessonTemplate(Long lessonTemplateId) {
        lessonTemplateRepository.findById(lessonTemplateId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LESSON_TEMPLATE_MISSING_PATTERN, lessonTemplateId)));
    }

    private void fillNullFields(LessonTemplate source, LessonTemplate destination) {
        if (isNull(destination.getId())) destination.setId(source.getId());
        if (isNull(destination.getTitle())) destination.setTitle(source.getTitle());
        if (isNull(destination.getDescription())) destination.setDescription(source.getDescription());
        if (isNull(destination.getNumber())) destination.setNumber(source.getNumber());
        if (isNull(destination.getMaxMark())) destination.setMaxMark(source.getMaxMark());
        if (isNull(destination.getSuccessMark())) destination.setSuccessMark(source.getSuccessMark());
        if (isNull(destination.getChapterTemplate())) destination.setChapterTemplate(source.getChapterTemplate());
    }
}
