package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ChapterTemplateMapper {

    private final ModelMapper modelMapper;

    public ChapterTemplateMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ChapterTemplateDto toDto(ChapterTemplate entity) {
        return modelMapper.map(entity, ChapterTemplateDto.class);
    }

    public ChapterTemplate fromDto(ChapterTemplateDto dto) {
        return modelMapper.map(dto, ChapterTemplate.class);
    }

    public ChapterTemplate fromDto(ChapterTemplateDtoSave dto) {
        return modelMapper.map(dto, ChapterTemplate.class);
    }

    public ChapterTemplate fromDtoUpdate(ChapterTemplateDtoUpdate dto) {
        return modelMapper.map(dto, ChapterTemplate.class);
    }
}
