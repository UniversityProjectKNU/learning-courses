package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.hoeworkfile.HomeworkFileDto;
import com.nazar.grynko.learningcourses.model.HomeworkFile;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class HomeworkFileMapper {

    private final ModelMapper modelMapper;

    public HomeworkFileMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public HomeworkFileDto toDto(HomeworkFile entity) {
        return modelMapper.map(entity, HomeworkFileDto.class);
    }
}
