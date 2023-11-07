package com.nazar.grynko.learningcourses.mapper;

import com.nazar.grynko.learningcourses.dto.enroll.EnrollRequestDto;
import com.nazar.grynko.learningcourses.model.EnrollRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EnrollRequestMapper {

    private final ModelMapper modelMapper;

    public EnrollRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public EnrollRequestDto toDto(EnrollRequest entity) {
        return modelMapper.map(entity, EnrollRequestDto.class);
    }

}
