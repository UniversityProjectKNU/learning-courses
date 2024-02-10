package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.mapper.LessonTemplateMapper;
import com.nazar.grynko.learningcourses.service.internal.ChapterTemplateInternalService;
import com.nazar.grynko.learningcourses.service.internal.CourseTemplateInternalService;
import com.nazar.grynko.learningcourses.service.internal.LessonTemplateInternalService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LessonTemplateService {

    private final CourseTemplateInternalService courseTemplateInternalService;
    private final ChapterTemplateInternalService chapterTemplateInternalService;
    private final LessonTemplateInternalService lessonTemplateInternalService;
    private final LessonTemplateMapper lessonTemplateMapper;

    public LessonTemplateService(CourseTemplateInternalService courseTemplateInternalService,
                                 ChapterTemplateInternalService chapterTemplateInternalService,
                                 LessonTemplateInternalService lessonTemplateInternalService,
                                 LessonTemplateMapper lessonTemplateMapper) {
        this.courseTemplateInternalService = courseTemplateInternalService;
        this.chapterTemplateInternalService = chapterTemplateInternalService;
        this.lessonTemplateInternalService = lessonTemplateInternalService;
        this.lessonTemplateMapper = lessonTemplateMapper;
    }

    public LessonTemplateDto get(Long id) {
        var lessonTemplate = lessonTemplateInternalService.get(id);
        return lessonTemplateMapper.toDto(lessonTemplate);
    }

    public List<LessonTemplateDto> getAllInCourseTemplate(Long courseTemplateId) {
        courseTemplateInternalService.throwIfMissingCourseTemplate(courseTemplateId);

        return lessonTemplateInternalService.getAllInCourseTemplate(courseTemplateId)
                .stream()
                .map(lessonTemplateMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<LessonTemplateDto> getAllInChapterTemplate(Long chapterTemplateId) {
        chapterTemplateInternalService.throwIfMissingChapterTemplate(chapterTemplateId);

        return lessonTemplateInternalService.getAllInChapterTemplate(chapterTemplateId)
                .stream()
                .map(lessonTemplateMapper::toDto)
                .collect(Collectors.toList());
    }

    public LessonTemplateDto save(LessonTemplateDtoSave dto, Long chapterTemplateId) {
        chapterTemplateInternalService.throwIfMissingChapterTemplate(chapterTemplateId);

        var entity = lessonTemplateMapper.fromDto(dto);
        entity = lessonTemplateInternalService.save(entity, chapterTemplateId);
        return lessonTemplateMapper.toDto(entity);
    }

    public void delete(Long lessonTemplateId) {
        lessonTemplateInternalService.throwIfMissingLessonTemplate(lessonTemplateId);

        lessonTemplateInternalService.delete(lessonTemplateId);
    }

    public LessonTemplateDto update(LessonTemplateDtoUpdate dto, Long lessonTemplateId) {
        lessonTemplateInternalService.throwIfMissingLessonTemplate(lessonTemplateId);

        var entity = lessonTemplateMapper.fromDtoUpdate(dto)
                .setId(lessonTemplateId);
        entity = lessonTemplateInternalService.update(entity);
        return lessonTemplateMapper.toDto(entity);
    }
}
