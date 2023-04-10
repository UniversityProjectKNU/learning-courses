package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateSave;
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

    public ChapterTemplate fromDto(ChapterTemplateSave dto) {
        return modelMapper.map(dto, ChapterTemplate.class);
    }

}
