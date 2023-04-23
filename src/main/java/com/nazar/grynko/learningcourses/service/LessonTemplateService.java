package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.mapper.LessonTemplateMapper;
import com.nazar.grynko.learningcourses.service.internal.LessonTemplateInternalService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LessonTemplateService {

    private final LessonTemplateInternalService lessonTemplateInternalService;
    private final LessonTemplateMapper lessonTemplateMapper;

    public LessonTemplateService(LessonTemplateInternalService lessonTemplateInternalService,
                                 LessonTemplateMapper lessonTemplateMapper) {
        this.lessonTemplateInternalService = lessonTemplateInternalService;
        this.lessonTemplateMapper = lessonTemplateMapper;
    }

    public Optional<LessonTemplateDto> get(Long id) {
        return lessonTemplateInternalService.get(id)
                .flatMap(val -> Optional.of(lessonTemplateMapper.toDto(val)));
    }

    public List<LessonTemplateDto> getAllInCourseTemplate(Long id) {
        return lessonTemplateInternalService.getAllInCourseTemplate(id)
                .stream()
                .map(lessonTemplateMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<LessonTemplateDto> getAllInChapterTemplate(Long chapterTemplateId) {
        return lessonTemplateInternalService.getAllInChapterTemplate(chapterTemplateId)
                .stream()
                .map(lessonTemplateMapper::toDto)
                .collect(Collectors.toList());
    }

    public LessonTemplateDto save(LessonTemplateDtoSave dto, Long chapterTemplateId) {
        var entity = lessonTemplateMapper.fromDto(dto);
        entity = lessonTemplateInternalService.save(entity, chapterTemplateId);
        return lessonTemplateMapper.toDto(entity);
    }

    public void delete(Long id) {
        lessonTemplateInternalService.delete(id);
    }

    public LessonTemplateDto update(LessonTemplateDtoUpdate dto) {
        var entity = lessonTemplateMapper.fromDtoUpdate(dto);
        entity = lessonTemplateInternalService.update(entity);
        return lessonTemplateMapper.toDto(entity);
    }

    public boolean hasWithCourseTemplate(Long id, Long chapterTemplateId, Long courseTemplateId) {
        return lessonTemplateInternalService.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId);
    }
}
