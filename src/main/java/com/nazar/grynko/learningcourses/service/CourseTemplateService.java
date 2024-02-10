package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDto;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoSave;
import com.nazar.grynko.learningcourses.dto.coursetemplate.CourseTemplateDtoUpdate;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.mapper.CourseTemplateMapper;
import com.nazar.grynko.learningcourses.mapper.UserMapper;
import com.nazar.grynko.learningcourses.service.internal.CourseTemplateInternalService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseTemplateService {

    private final CourseTemplateInternalService courseTemplateInternalService;
    private final CourseTemplateMapper courseTemplateMapper;
    private final UserMapper userMapper;


    public CourseTemplateService(CourseTemplateInternalService courseTemplateInternalService,
                                 CourseTemplateMapper courseTemplateMapper,
                                 UserMapper userMapper) {
        this.courseTemplateInternalService = courseTemplateInternalService;
        this.courseTemplateMapper = courseTemplateMapper;
        this.userMapper = userMapper;
    }

    public CourseTemplateDto get(Long courseTemplateId) {
        var courseTemplate = courseTemplateInternalService.get(courseTemplateId);
        return courseTemplateMapper.toDto(courseTemplate);
    }

    public List<CourseTemplateDto> getAll() {
        return courseTemplateInternalService.getAll()
                .stream()
                .map(courseTemplateMapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long courseTemplateId) {
        courseTemplateInternalService.delete(courseTemplateId);
    }

    public CourseTemplateDto save(CourseTemplateDtoSave dto) {
        var entity = courseTemplateMapper.fromDto(dto);
        entity = courseTemplateInternalService.save(entity);
        return courseTemplateMapper.toDto(entity);
    }

    public CourseTemplateDto update(CourseTemplateDtoUpdate dto, Long courseTemplateId) {
        var entity = courseTemplateMapper.fromDtoUpdate(dto)
                .setId(courseTemplateId);
        entity = courseTemplateInternalService.update(entity);
        return courseTemplateMapper.toDto(entity);
    }

    public UserDto getCourseTemplateOwner(Long courseTemplateId) {
        var owner = courseTemplateInternalService.getCourseTemplateOwner(courseTemplateId);
        return userMapper.toDto(owner);
    }
}
