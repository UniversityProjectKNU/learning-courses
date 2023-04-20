package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoSave;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.mapper.CourseMapper;
import com.nazar.grynko.learningcourses.mapper.UserToCourseMapper;
import com.nazar.grynko.learningcourses.service.internal.CourseInternalService;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CourseService {

    private final CourseInternalService courseInternalService;
    private final CourseMapper courseMapper;
    private final UserToCourseMapper userToCourseMapper;
    private final UserInternalService userInternalService;

    public CourseService(CourseInternalService courseInternalService, CourseMapper courseMapper,
                         UserToCourseMapper userToCourseMapper, UserInternalService userInternalService) {
        this.courseInternalService = courseInternalService;
        this.courseMapper = courseMapper;
        this.userToCourseMapper = userToCourseMapper;
        this.userInternalService = userInternalService;
    }

    public Optional<CourseDto> get(Long id) {
        return courseInternalService.get(id)
                .flatMap(val -> Optional.of(courseMapper.toDto(val)));
    }

    public List<CourseDto> getAll() {
        return courseInternalService.getAll()
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        courseInternalService.delete(id);
    }

    public CourseDto create(Long courseTemplateId) {
        var course = courseInternalService.create(courseTemplateId);
        return courseMapper.toDto(course);
    }

    public CourseDto save(CourseDtoSave dto) {
        var entity = courseMapper.fromDtoSave(dto);
        entity.setIsFinished(false);

        return courseMapper.toDto(courseInternalService.save(entity));
    }

    public CourseDto update(CourseDto dto, Long courseId) {
        var entity = courseMapper.fromDto(dto).setId(courseId);
        entity = courseInternalService.update(entity);
        return courseMapper.toDto(entity);
    }

    public UserToCourseDto enroll(Long id, String login) {
        var user = userInternalService.getByLogin(login).orElseThrow(IllegalAccessError::new);
        var course = courseInternalService.get(id).orElseThrow(IllegalArgumentException::new);

        var entity = courseInternalService.enroll(user, course);
        return userToCourseMapper.toDto(entity);
    }
}
