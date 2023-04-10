package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateSave;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.service.internal.ChapterTemplateInternalService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ChapterTemplateService {

    private final ChapterTemplateInternalService chapterTemplateInternalService;
    private final ModelMapper modelMapper;

    public ChapterTemplateService(ChapterTemplateInternalService chapterTemplateInternalService, ModelMapper modelMapper) {
        this.chapterTemplateInternalService = chapterTemplateInternalService;
        this.modelMapper = modelMapper;
    }

    public Optional<ChapterTemplateDto> get(Long id) {
        return chapterTemplateInternalService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public void delete(Long id) {
        chapterTemplateInternalService.delete(id);
    }

    public ChapterTemplateDto save(ChapterTemplateSave dto, Long courseTemplateId) {
        ChapterTemplate entity = fromDto(dto);
        entity = chapterTemplateInternalService.save(entity, courseTemplateId);
        return toDto(entity);
    }

    public ChapterTemplateDto update(ChapterTemplateDto dto) {
        ChapterTemplate chapterTemplate = fromDto(dto);
        chapterTemplate = chapterTemplateInternalService.update(chapterTemplate);
        return toDto(chapterTemplate);
    }

    public List<ChapterTemplateDto> getAllInCourseTemplate(Long courseTemplateId) {
        List<ChapterTemplate> chapterTemplates = chapterTemplateInternalService.getAllInCourseTemplate(courseTemplateId);
        return chapterTemplates.stream().map(this::toDto).collect(Collectors.toList());
    }

    public boolean hasWithCourseTemplate(Long chapterTemplateId, Long courseTemplateId) {
        return chapterTemplateInternalService.hasWithCourseTemplate(chapterTemplateId, courseTemplateId);
    }

    private ChapterTemplateDto toDto(ChapterTemplate entity) {
        return modelMapper.map(entity, ChapterTemplateDto.class);
    }

    private ChapterTemplate fromDto(ChapterTemplateDto dto) {
        return modelMapper.map(dto, ChapterTemplate.class);
    }

    private ChapterTemplate fromDto(ChapterTemplateSave dto) {
        return modelMapper.map(dto, ChapterTemplate.class);
    }

}
