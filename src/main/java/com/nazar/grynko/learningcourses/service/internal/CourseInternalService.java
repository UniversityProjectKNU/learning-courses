package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.mapper.CourseMapper;
import com.nazar.grynko.learningcourses.model.*;
import com.nazar.grynko.learningcourses.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseInternalService {

    private final CourseTemplateInternalService courseTemplateInternalService;
    private final LessonTemplateInternalService lessonTemplateInternalService;
    private final UserInternalService userInternalService;
    private final ChapterInternalService chapterInternalService;
    private final LessonInternalService lessonInternalService;
    private final CourseMapper courseMapper;
    private final UserToCourseInternalService userToCourseInternalService;
    private final UserToLessonInternalService userToLessonInternalService;
    private final CourseRepository courseRepository;

    @Value("${max.courses.number.at.time}")
    private Integer MAX_COURSES_NUMBER;
    @Value("${min.lessons.number.in.course}")
    private Integer MIN_LESSONS_NUMBER;

    public CourseInternalService(CourseRepository courseRepository,
                                 CourseTemplateInternalService courseTemplateInternalService,
                                 LessonTemplateInternalService lessonTemplateInternalService,
                                 UserInternalService userInternalService,
                                 ChapterInternalService chapterInternalService,
                                 LessonInternalService lessonInternalService,
                                 CourseMapper courseMapper,
                                 UserToCourseInternalService userToCourseInternalService,
                                 UserToLessonInternalService userToLessonInternalService) {
        this.courseRepository = courseRepository;
        this.courseTemplateInternalService = courseTemplateInternalService;
        this.lessonTemplateInternalService = lessonTemplateInternalService;
        this.userInternalService = userInternalService;
        this.chapterInternalService = chapterInternalService;
        this.lessonInternalService = lessonInternalService;
        this.courseMapper = courseMapper;
        this.userToCourseInternalService = userToCourseInternalService;
        this.userToLessonInternalService = userToLessonInternalService;
    }

    public Optional<Course> get(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public void delete(Long id) {
        var course = courseRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        courseRepository.delete(course);
    }

    @Transactional
    public Course create(Long courseTemplateId, String login) {
        if (!isValidAmountOfLessons(courseTemplateId)) {
            throw new IllegalStateException(String.format(
                    "Course must contain at least %d lessons", MIN_LESSONS_NUMBER));
        }

        var template = courseTemplateInternalService.get(courseTemplateId)
                .orElseThrow(IllegalArgumentException::new);

        var user = userInternalService.getByLogin(login).orElseThrow(IllegalArgumentException::new);
        var course = courseMapper.fromTemplate(template)
                .setId(null)
                .setIsFinished(false);

        course = courseRepository.save(course);
        chapterInternalService.create(template.getId(), course);

        var userToCourse = new UserToCourse()
                .setCourse(course)
                .setUser(user)
                .setMark(0f)
                .setIsPassed(false);

        userToCourseInternalService.save(userToCourse);

        return course;
    }

    @Transactional
    public Course finish(Long id) {
        var entity = get(id).orElseThrow(IllegalArgumentException::new);
        if (entity.getIsFinished()) {
            throw new IllegalStateException("Course was already finished");
        }

        entity.setIsFinished(true);
        courseRepository.save(entity);

        chapterInternalService.finish(id);
        userToCourseInternalService.finish(id);

        userToLessonInternalService.setIsPassedForLessonsInCourse(id);

        return entity;
    }

    public Course save(Course entity) {
        return courseRepository.save(entity);
    }

    public Course update(Course course) {
        var dbCourse = courseRepository.findById(course.getId())
                .orElseThrow(IllegalArgumentException::new);
        fillNullFields(dbCourse, course);
        return courseRepository.save(course);
    }

    @Transactional
    public UserToCourse enroll(Long courseId, String login) {
        var user = userInternalService.getByLogin(login).orElseThrow(
                () -> new IllegalArgumentException("User doesn't exist"));
        var course = get(courseId).orElseThrow(
                () -> new IllegalArgumentException("Course doesn't exist"));

        if (!isValidAmountOfCourses(user)) {
            throw new IllegalStateException(
                    String.format("User %d already has max amount of courses (%d)", user.getId(), MAX_COURSES_NUMBER));
        }

        var entity = new UserToCourse()
                .setUser(user)
                .setCourse(course)
                .setMark(0f)
                .setIsPassed(false);

        entity = userToCourseInternalService.save(entity);
        lessonInternalService.enroll(user, course.getId());

        return entity;
    }

    public UserToCourse assignInstructor(Long id, Long instructorId) {
        var user = userInternalService.get(instructorId).orElseThrow(IllegalAccessError::new);

        if (user.getRole().equals(RoleType.INSTRUCTOR)) {
            throw new IllegalArgumentException("Cannot assign not instructor");
        }

        var optional = userToCourseInternalService.getByUserIdAndCourseId(instructorId, id);
        if (optional.isPresent()) {
            throw new IllegalArgumentException(String.format(
                    "Instructor %d was already assign to the course %d", instructorId, id));
        }

        var course = get(id).orElseThrow(IllegalArgumentException::new);

        var entity = new UserToCourse()
                .setUser(user)
                .setCourse(course)
                .setMark(0f)
                .setIsPassed(false);

        return userToCourseInternalService.save(entity);
    }

    private boolean isValidAmountOfCourses(User user) {
        var courses = userToCourseInternalService.getAllByUserId(user.getId());
        var activeCoursesAmount = (int) courses.stream()
                .filter(e -> !e.getIsPassed())
                .count();
        return activeCoursesAmount < MAX_COURSES_NUMBER;
    }

    private boolean isValidAmountOfLessons(Long courseTemplateId) {
        var lessonsTemplates = lessonTemplateInternalService.getAllInCourseTemplate(courseTemplateId);
        return lessonsTemplates.size() >= MIN_LESSONS_NUMBER;
    }

    public List<Course> getUsersCourses(String login, Boolean isFinished) {
        var userId = userInternalService.getByLogin(login).orElseThrow(IllegalArgumentException::new).getId();
        var courses = userToCourseInternalService.getAllByUserId(userId)
                .stream()
                .map(UserToCourse::getCourse);

        if (isFinished != null) {
            courses = courses.filter(c -> c.getIsFinished() == isFinished);
        }

        return courses.collect(Collectors.toList());
    }

    public UserToCourse getUsersCourseInfo(Long id, Long userId) {
        return userToCourseInternalService.getByUserIdAndCourseId(userId, id)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't have this course"));
    }

    private void fillNullFields(Course source, Course destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getTitle() == null) destination.setTitle(source.getTitle());
        if (destination.getDescription() == null) destination.setDescription(source.getDescription());
        if (destination.getIsFinished() == null) destination.setIsFinished(source.getIsFinished());
    }

    public boolean courseExists(Long id) {
        return get(id).isPresent();
    }

}
