package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoSave;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseInfoDto;
import com.nazar.grynko.learningcourses.mapper.CourseMapper;
import com.nazar.grynko.learningcourses.mapper.UserToCourseMapper;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.service.internal.CourseInternalService;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import com.nazar.grynko.learningcourses.service.internal.UserToCourseInternalService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class CourseService {

    private final CourseInternalService courseInternalService;
    private final UserInternalService userInternalService;
    private final UserToCourseInternalService userToCourseInternalService;
    private final CourseMapper courseMapper;
    private final UserToCourseMapper userToCourseMapper;

    public CourseService(CourseInternalService courseInternalService,
                         UserInternalService userInternalService,
                         UserToCourseInternalService userToCourseInternalService,
                         CourseMapper courseMapper,
                         UserToCourseMapper userToCourseMapper) {
        this.courseInternalService = courseInternalService;
        this.userInternalService = userInternalService;
        this.userToCourseInternalService = userToCourseInternalService;
        this.courseMapper = courseMapper;
        this.userToCourseMapper = userToCourseMapper;
    }

    public Optional<CourseDto> get(Long id) {
        return courseInternalService.get(id)
                .flatMap(val -> Optional.of(courseMapper.toDto(val)));
    }

    public List<CourseDto> getAll(Boolean isActive) {
        return courseInternalService.getAll()
                .stream()
                .filter(c -> isNull(isActive) || !c.getIsFinished().equals(isActive))
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        courseInternalService.delete(id);
    }

    public CourseDto create(Long courseTemplateId, String login) {
        var course = courseInternalService.create(courseTemplateId, login);
        return courseMapper.toDto(course);
    }

    public CourseDto save(CourseDtoSave dto) {
        var entity = courseMapper.fromDtoSave(dto);
        entity.setIsFinished(false);

        return courseMapper.toDto(courseInternalService.save(entity));
    }

    public CourseDto update(CourseDtoUpdate dto, Long courseId) {
        var entity = courseMapper.fromDtoUpdate(dto).setId(courseId);
        entity = courseInternalService.update(entity);
        return courseMapper.toDto(entity);
    }

    public UserToCourseDto enroll(Long id, String login) {
        var entity = courseInternalService.enroll(id, login);
        return userToCourseMapper.toDto(entity);
    }

    public List<UserToCourseInfoDto> getAllUserToCourseInfoForCourse(Long id, RoleType roleType) {
        if (roleType == RoleType.ADMIN) {
            throw new IllegalArgumentException(String.format(
                    "Cannot get courses for role %s", roleType.name()));
        }

        var types = roleType == null ? List.of(RoleType.STUDENT, RoleType.INSTRUCTOR) : List.of(roleType);

        return userToCourseInternalService.getAllByCourseId(id)
                .stream()
                .filter(userToCourse -> types.contains(userToCourse.getUser().getRole()))
                .map(userToCourseMapper::toUserToCourseInfoDto)
                .collect(Collectors.toList());
    }

    public UserToCourseDto assignInstructor(Long id, Long instructorId) {
        var userToCourse = courseInternalService.assignInstructor(id, instructorId);
        return userToCourseMapper.toDto(userToCourse);
    }

    public List<CourseDto> getUsersCourses(String login, Boolean isFinished) {
        var courses = courseInternalService.getUsersCourses(login, isFinished);
        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserToCourseDto getUsersCourseInfo(Long id, String login) {
        var userId = userInternalService.getByLogin(login).orElseThrow(IllegalArgumentException::new).getId();

        var userToCourse = courseInternalService.getUsersCourseInfo(id, userId);
        return userToCourseMapper.toDto(userToCourse);
    }

    public UserToCourseDto getUsersCourseInfo(Long id, Long userId) {
        userInternalService.get(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User %d doesn't exist", userId)));

        var userToCourse = courseInternalService.getUsersCourseInfo(id, userId);
        return userToCourseMapper.toDto(userToCourse);
    }

    public UserToCourseDto updateUserToCourse(Long id, Long userId, UserToCourseDtoUpdate dto) {
        var entity = userToCourseMapper.fromDtoUpdate(dto).setId(id);
        entity = userToCourseInternalService.update(userId, id, entity);
        return userToCourseMapper.toDto(entity);
    }

    public CourseDto finish(Long id) {
        courseInternalService.get(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Course %d doesn't exist", id)));
        var entity = courseInternalService.finish(id);
        return courseMapper.toDto(entity);
    }

}