package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoSave;
import com.nazar.grynko.learningcourses.dto.course.CourseDtoUpdate;
import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.usertocourse.UserToCourseDto;
import com.nazar.grynko.learningcourses.mapper.*;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.service.internal.ChapterInternalService;
import com.nazar.grynko.learningcourses.service.internal.CourseInternalService;
import com.nazar.grynko.learningcourses.service.internal.LessonInternalService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CourseService {

    private final CourseInternalService courseInternalService;
    private final ChapterInternalService chapterInternalService;
    private final LessonInternalService lessonInternalService;
    private final CourseMapper courseMapper;
    private final ChapterMapper chapterMapper;
    private final LessonMapper lessonMapper;
    private final UserToCourseMapper userToCourseMapper;
    private final UserMapper userMapper;

    public CourseService(CourseInternalService courseInternalService,
                         ChapterInternalService chapterInternalService,
                         LessonInternalService lessonInternalService,
                         CourseMapper courseMapper,
                         ChapterMapper chapterMapper,
                         LessonMapper lessonMapper,
                         UserToCourseMapper userToCourseMapper,
                         UserMapper userMapper) {
        this.courseInternalService = courseInternalService;
        this.chapterInternalService = chapterInternalService;
        this.lessonInternalService = lessonInternalService;
        this.courseMapper = courseMapper;
        this.chapterMapper = chapterMapper;
        this.lessonMapper = lessonMapper;
        this.userToCourseMapper = userToCourseMapper;
        this.userMapper = userMapper;
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

    public List<UserDto> getAllInstructorsForCourse(Long id, RoleType roleType) {
        if (roleType == RoleType.ADMIN) {
            throw new IllegalArgumentException(String.format(
                    "Cannot get courses for role %s", roleType.name()));
        }

        var types = roleType == null ? List.of(RoleType.STUDENT, RoleType.INSTRUCTOR) : List.of(roleType);

        return courseInternalService.getAllUsersForCourseByRole(id, types)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserToCourseDto assignInstructor(Long id, Long instructorId) {
        var userToCourse = courseInternalService.assignInstructor(id, instructorId);
        return userToCourseMapper.toDto(userToCourse);
    }

    public List<ChapterDto> getAllChapters(Long courseId) {
        return chapterInternalService.getAllInCourse(courseId)
                .stream()
                .map(chapterMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getAllLessons(Long courseId) {
        return lessonInternalService.getAllInCourse(courseId)
                .stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }
}
