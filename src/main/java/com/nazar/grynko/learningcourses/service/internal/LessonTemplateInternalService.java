package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.repository.LessonTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class LessonTemplateInternalService {

    private final LessonTemplateRepository lessonTemplateRepository;
    private final ChapterTemplateInternalService chapterTemplateInternalService;
    private final CourseTemplateInternalService courseTemplateInternalService;

    @Autowired
    public LessonTemplateInternalService(LessonTemplateRepository lessonTemplateRepository,
                                         ChapterTemplateInternalService chapterTemplateInternalService,
                                         CourseTemplateInternalService courseTemplateInternalService) {
        this.lessonTemplateRepository = lessonTemplateRepository;
        this.chapterTemplateInternalService = chapterTemplateInternalService;
        this.courseTemplateInternalService = courseTemplateInternalService;
    }

    public Optional<LessonTemplate> get(Long id) {
        return lessonTemplateRepository.findById(id);
    }

    public void delete(Long id) {
        lessonTemplateRepository.deleteById(id);
    }

    public LessonTemplate save(LessonTemplate entity, Long chapterTemplateId) {
        ChapterTemplate chapterTemplate = chapterTemplateInternalService.get(chapterTemplateId)
                .orElseThrow(InvalidPathException::new);
        entity.setChapterTemplate(chapterTemplate);

        return lessonTemplateRepository.save(entity);
    }

    public LessonTemplate update(LessonTemplate entity) {
        LessonTemplate dbLessonTemplate = lessonTemplateRepository.findById(entity.getId())
                .orElseThrow(IllegalArgumentException::new);
        fillNullFields(dbLessonTemplate, entity);
        return lessonTemplateRepository.save(entity);
    }

    public List<LessonTemplate> getAllInChapterTemplate(Long chapterTemplateId) {
        chapterTemplateInternalService.get(chapterTemplateId)
                .orElseThrow(() -> new IllegalArgumentException("No chapter template with id: " + chapterTemplateId));
        return lessonTemplateRepository.getLessonTemplatesByChapterTemplateId(chapterTemplateId);
    }

    public List<LessonTemplate> getAllInCourseTemplate(Long courseTemplateId) {
        courseTemplateInternalService.get(courseTemplateId)
                .orElseThrow(() -> new IllegalArgumentException("No course template with id: " + courseTemplateId));
        return lessonTemplateRepository.getAllInCourse(courseTemplateId);
    }

    public boolean hasWithCourseTemplate(Long id, Long chapterTemplateId, Long courseTemplateId) {
        Optional<LessonTemplate> optional = lessonTemplateRepository
                .getLessonTemplateByIdAndChapterTemplateIdAndChapterTemplateCourseTemplateId(id, chapterTemplateId, courseTemplateId);

        return optional.isPresent();
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
