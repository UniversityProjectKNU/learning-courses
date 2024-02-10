package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.mapper.ChapterTemplateMapper;
import com.nazar.grynko.learningcourses.service.internal.ChapterTemplateInternalService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChapterTemplateService {

    private final ChapterTemplateInternalService chapterTemplateInternalService;
    private final ChapterTemplateMapper chapterTemplateMapper;

    public ChapterTemplateService(ChapterTemplateInternalService chapterTemplateInternalService,
                                  ChapterTemplateMapper chapterTemplateMapper) {
        this.chapterTemplateInternalService = chapterTemplateInternalService;
        this.chapterTemplateMapper = chapterTemplateMapper;
    }

    public ChapterTemplateDto get(Long chapterTemplateId) {
        var entity = chapterTemplateInternalService.get(chapterTemplateId);
        return chapterTemplateMapper.toDto(entity);
    }

    public void delete(Long chapterTemplateId) {
        chapterTemplateInternalService.delete(chapterTemplateId);
    }

    public ChapterTemplateDto save(ChapterTemplateDtoSave dto, Long courseTemplateId) {
        var entity = chapterTemplateMapper.fromDto(dto);
        entity = chapterTemplateInternalService.save(entity, courseTemplateId);
        return chapterTemplateMapper.toDto(entity);
    }

    public ChapterTemplateDto update(ChapterTemplateDtoUpdate dto, Long chapterTemplateId) {
        var chapterTemplate = chapterTemplateMapper.fromDtoUpdate(dto)
                .setId(chapterTemplateId);
        chapterTemplate = chapterTemplateInternalService.update(chapterTemplate);
        return chapterTemplateMapper.toDto(chapterTemplate);
    }

    public List<ChapterTemplateDto> getAllInCourseTemplate(Long courseTemplateId) {
        var chapterTemplates = chapterTemplateInternalService.getAllInCourseTemplate(courseTemplateId);
        return chapterTemplates.stream()
                .map(chapterTemplateMapper::toDto)
                .collect(Collectors.toList());
    }

}
