package com.nazar.grynko.learningcourses.wrapper;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateSave;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LessonTemplateServiceWrapper {

    private final LessonTemplateService lessonTemplateService;
    private final ModelMapper modelMapper;

    public LessonTemplateServiceWrapper(LessonTemplateService lessonTemplateService, ModelMapper modelMapper) {
        this.lessonTemplateService = lessonTemplateService;
        this.modelMapper = modelMapper;
    }

    public Optional<LessonTemplateDto> get(Long id) {
        return lessonTemplateService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public LessonTemplateDto save(LessonTemplateSave dto, Long chapterTemplateId) {
        LessonTemplate entity = fromDto(dto);
        entity = lessonTemplateService.save(entity, chapterTemplateId);
        return toDto(entity);
    }

    public void delete(Long id) {
        lessonTemplateService.delete(id);
    }

    public LessonTemplateDto update(LessonTemplateDto dto) {
        LessonTemplate entity = fromDto(dto);
        entity = lessonTemplateService.update(entity);
        return toDto(entity);
    }

    public List<LessonTemplateDto> getAllInChapterTemplate(Long chapterTemplateId) {
        return lessonTemplateService.getAllInChapterTemplate(chapterTemplateId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public boolean hasWithCourseTemplate(Long id, Long chapterTemplateId, Long courseTemplateId) {
        return lessonTemplateService.hasWithCourseTemplate(id, chapterTemplateId, courseTemplateId);
    }

    private LessonTemplate fromDto(LessonTemplateDto dto) {
        return modelMapper.map(dto, LessonTemplate.class);
    }

    private LessonTemplate fromDto(LessonTemplateSave dto) {
        return modelMapper.map(dto, LessonTemplate.class);
    }

    private LessonTemplateDto toDto(LessonTemplate entity) {
        return modelMapper.map(entity, LessonTemplateDto.class);
    }

}
