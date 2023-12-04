package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.enroll.EnrollRequestDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseInfoDto;
import com.nazar.grynko.learningcourses.mapper.*;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.service.internal.CourseInternalService;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import com.nazar.grynko.learningcourses.service.internal.UserToCourseInternalService;
import org.springframework.stereotype.Component;

import java.util.List;
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
    private final UserMapper userMapper;
    private final EnrollRequestMapper enrollRequestMapper;

    public CourseService(CourseInternalService courseInternalService,
                         UserInternalService userInternalService,
                         UserToCourseInternalService userToCourseInternalService,
                         CourseMapper courseMapper,
                         UserToCourseMapper userToCourseMapper,
                         UserMapper userMapper,
                         EnrollRequestMapper enrollRequestMapper) {
        this.courseInternalService = courseInternalService;
        this.userInternalService = userInternalService;
        this.userToCourseInternalService = userToCourseInternalService;
        this.courseMapper = courseMapper;
        this.userToCourseMapper = userToCourseMapper;
        this.userMapper = userMapper;
        this.enrollRequestMapper = enrollRequestMapper;
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

/*    public CourseDto save(CourseDtoSave dto) {
        var entity = courseMapper.fromDtoSave(dto);
        entity.setIsFinished(false);

        return courseMapper.toDto(courseInternalService.save(entity));
    }*/

    public CourseDto update(CourseDtoUpdate dto, Long courseId) {
        var entity = courseMapper.fromDtoUpdate(dto).setId(courseId);
        entity = courseInternalService.update(entity);
        return courseMapper.toDto(entity);
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

    public UserToCourseDto enroll(Long courseId, Long userId) {
        var userToCourse = courseInternalService.enroll(courseId, userId);
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

        var userToCourse = userToCourseInternalService.getByUserIdAndCourseId(userId, id)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't have this course"));
        return userToCourseMapper.toDto(userToCourse);
    }

    public UserToCourseDto getUsersCourseInfo(Long id, Long userId) {
        userInternalService.get(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User %d doesn't exist", userId)));

        var userToCourse = userToCourseInternalService.getByUserIdAndCourseId(userId, id)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't have this course"));
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

    public UserDto getCourseOwner(Long courseId) {
        var owner = courseInternalService.getCourseOwner(courseId);
        return userMapper.toDto(owner);
    }

    public void removeUserFromCourse(Long courseId, Long userId) {
        userToCourseInternalService.removeUserFromCourse(courseId, userId);
    }

    public EnrollRequestDto sendEnrollRequest(Long courseId, String login) {
        var entity = courseInternalService.sendEnrollRequest(courseId, login);
        return enrollRequestMapper.toDto(entity);
    }

    public List<EnrollRequestDto> getAllEnrollRequests(Long courseId, Boolean isActive) {
        return courseInternalService.getAllEnrollRequestsForCourse(courseId, isActive)
                .stream()
                .map(enrollRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserToCourseDto approveEnrollRequest(Long enrollRequestId, Boolean isApproved) {
        var entity = courseInternalService.approveEnrollRequest(enrollRequestId, isApproved);
        return userToCourseMapper.toDto(entity);
    }

    public EnrollRequestDto getUsersLastEnrollRequest(Long userId, Long courseId) {
        var entity = courseInternalService.getUsersLastEnrollRequest(userId, courseId);
        return enrollRequestMapper.toDto(entity);
    }
}