package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.dto.chapter.ChapterDtoSave;
import com.nazar.grynko.learningcourses.dto.chapter.ChapterDtoUpdate;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ChapterMapper {

    private final ModelMapper modelMapper;

    public ChapterMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ChapterDto toDto(Chapter entity) {
        return modelMapper.map(entity, ChapterDto.class);
    }

    public Chapter fromDto(ChapterDto dto) {
        return modelMapper.map(dto, Chapter.class);
    }

    public Chapter fromDtoSave(ChapterDtoSave dto) {
        return modelMapper.map(dto, Chapter.class);
    }

    public Chapter fromDtoUpdate(ChapterDtoUpdate dto) {
        return modelMapper.map(dto, Chapter.class);
    }

    public Chapter fromTemplate(ChapterTemplate template) {
        return modelMapper.map(template, Chapter.class);
    }

}
