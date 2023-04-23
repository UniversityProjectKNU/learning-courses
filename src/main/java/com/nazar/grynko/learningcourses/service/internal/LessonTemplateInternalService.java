package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.repository.LessonTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonTemplateInternalService {

    private final LessonTemplateRepository lessonTemplateRepository;
    private final ChapterTemplateInternalService chapterTemplateInternalService;

    @Autowired
    public LessonTemplateInternalService(LessonTemplateRepository lessonTemplateRepository,
                                         ChapterTemplateInternalService chapterTemplateInternalService) {
        this.lessonTemplateRepository = lessonTemplateRepository;
        this.chapterTemplateInternalService = chapterTemplateInternalService;
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
                .orElseThrow(IllegalArgumentException::new);
        return lessonTemplateRepository.getLessonTemplatesByChapterTemplateId(chapterTemplateId);
    }

    public List<LessonTemplate> getAllInCourseTemplate(Long courseTemplateId) {
        return lessonTemplateRepository.getAllInCourse(courseTemplateId);
    }

    public boolean hasWithCourseTemplate(Long id, Long chapterTemplateId, Long courseTemplateId) {
        Optional<LessonTemplate> optional = lessonTemplateRepository
                .getLessonTemplateByIdAndChapterTemplateIdAndChapterTemplateCourseTemplateId(id, chapterTemplateId, courseTemplateId);

        return optional.isPresent();
    }

    private void fillNullFields(LessonTemplate source, LessonTemplate destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getTitle() == null) destination.setTitle(source.getTitle());
        if (destination.getDescription() == null) destination.setDescription(source.getDescription());
        if (destination.getNumber() == null) destination.setNumber(source.getNumber());
        if (destination.getChapterTemplate() == null) destination.setChapterTemplate(source.getChapterTemplate());
    }
}
