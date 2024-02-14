package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.mapper.CourseMapper;
import com.nazar.grynko.learningcourses.model.*;
import com.nazar.grynko.learningcourses.repository.CourseOwnerRepository;
import com.nazar.grynko.learningcourses.repository.CourseRepository;
import com.nazar.grynko.learningcourses.repository.EnrollRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
    private final CourseOwnerRepository courseOwnerRepository;
    private final EnrollRequestRepository enrollRequestRepository;

    private static final String COURSE_MISSING_PATTERN = "Course %d doesn't exist";
    private static final String ENROLL_REQUEST_MISSING_PATTERN = "Enroll request %d doesn't exist";
    private static final String STUDENT_MAX_AMOUNT_OF_ACTIVE_PATTERN = "Student %d already has max amount of active courses (%d)";

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
                                 UserToLessonInternalService userToLessonInternalService,
                                 CourseOwnerRepository courseOwnerRepository,
                                 EnrollRequestRepository enrollRequestRepository) {
        this.courseRepository = courseRepository;
        this.courseTemplateInternalService = courseTemplateInternalService;
        this.lessonTemplateInternalService = lessonTemplateInternalService;
        this.userInternalService = userInternalService;
        this.chapterInternalService = chapterInternalService;
        this.lessonInternalService = lessonInternalService;
        this.courseMapper = courseMapper;
        this.userToCourseInternalService = userToCourseInternalService;
        this.userToLessonInternalService = userToLessonInternalService;
        this.courseOwnerRepository = courseOwnerRepository;
        this.enrollRequestRepository = enrollRequestRepository;
    }

    public Course get(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(COURSE_MISSING_PATTERN, courseId)));
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public void delete(Long courseId) {
        var course = get(courseId);
        courseRepository.delete(course);
    }

    @Transactional
    public Course create(Long courseTemplateId, String login) {
        if (!isValidAmountOfLessons(courseTemplateId)) {
            throw new IllegalStateException(String.format(
                    "Course must contain at least %d lessons", MIN_LESSONS_NUMBER));
        }

        var template = courseTemplateInternalService.get(courseTemplateId);

        var user = userInternalService.getByLogin(login);
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

        var courseOwner = new CourseOwner()
                .setCourse(course)
                .setUser(user);
        courseOwnerRepository.save(courseOwner);

        return course;
    }

    @Transactional //todo check if it works fine
    public Course finish(Long id) {
        var entity = get(id);
        if (entity.getIsFinished()) {
            throw new IllegalStateException("Course is already finished");
        }

        entity.setIsFinished(true);
        courseRepository.save(entity);

        //todo does it works fine because we save it implicitly in db
        userToLessonInternalService.setIsPassedForLessonsInCourse(id);

        chapterInternalService.finish(id);
        userToCourseInternalService.finish(id);

        return entity;
    }

    public Course update(Course course) {
        var dbCourse = get(course.getId());
        fillNullFields(dbCourse, course);

        return courseRepository.save(course);
    }

    @Transactional
    public UserToCourse enroll(Long courseId, Long userId) {
        var user = userInternalService.get(userId);

        if (user.getRole().equals(RoleType.ADMIN)) {
            throw new IllegalStateException("You cannot enroll ADMIN to course");
        }

        if (userToCourseInternalService.existsByUserIdAndCourseId(userId, courseId)) {
            throw new IllegalStateException(String.format(
                    "User (%s) %d was already assign to the course %d", user.getRole().name(), userId, courseId));
        }

        var course = get(courseId);

        var entity = new UserToCourse()
                .setUser(user)
                .setCourse(course)
                .setMark(0f)
                .setIsPassed(false);

        if (user.getRole().equals(RoleType.STUDENT)) {
            if (!isValidAmountOfCourses(user)) {
                throw new IllegalStateException(
                        String.format(STUDENT_MAX_AMOUNT_OF_ACTIVE_PATTERN, user.getId(), MAX_COURSES_NUMBER));
            }

            entity = userToCourseInternalService.save(entity);
            lessonInternalService.enroll(user, course.getId());

            return entity;
        }
        return userToCourseInternalService.save(entity);
    }

    private boolean isValidAmountOfCourses(User user) {
        var courses = userToCourseInternalService.getAllByUserId(user.getId());
        var activeCoursesAmount = (int) courses.stream()
                .filter(e -> !e.getCourse().getIsFinished())
                .count();
        return activeCoursesAmount < MAX_COURSES_NUMBER;
    }

    private boolean isValidAmountOfLessons(Long courseTemplateId) {
        var lessonsTemplates = lessonTemplateInternalService.getNumberOfLessonsForCourseTemplate(courseTemplateId);
        return lessonsTemplates >= MIN_LESSONS_NUMBER;
    }

    public List<Course> getAllUsersCourses(String login, Boolean isActive) {
        var userId = userInternalService.getByLogin(login)
                .getId();
        var courses = userToCourseInternalService.getAllByUserId(userId)
                .stream()
                .map(UserToCourse::getCourse);

        if (isActive != null) {
            courses = courses.filter(c -> c.getIsFinished() != isActive);
        }

        return courses.collect(Collectors.toList());
    }

    public User getCourseOwner(Long courseId) {
        return courseOwnerRepository.getCourseOwnerByCourseId(courseId).getUser();
    }

    public void throwIfMissingCourse(Long courseId) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(COURSE_MISSING_PATTERN, courseId)));
    }

    private void fillNullFields(Course source, Course destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getTitle() == null) destination.setTitle(source.getTitle());
        if (destination.getDescription() == null) destination.setDescription(source.getDescription());
        if (destination.getIsFinished() == null) destination.setIsFinished(source.getIsFinished());
    }

    public EnrollRequest sendEnrollRequest(Long courseId, String login) {
        if (userToCourseInternalService.existsByLoginAndCourseId(login, courseId)) {
            throw new IllegalStateException("User is already enrolled");
        }

        var enrollRequest = enrollRequestRepository.getByCourseIdAndUserLoginAndIsActiveTrue(courseId, login);
        if (nonNull(enrollRequest)) {
            throw new IllegalStateException(String.format("User already has an active request: %d", enrollRequest.getId()));
        }

        var user = userInternalService.getByLogin(login);

        if (user.getRole().equals(RoleType.STUDENT) && !isValidAmountOfCourses(user)) {
            throw new IllegalStateException(
                    String.format(STUDENT_MAX_AMOUNT_OF_ACTIVE_PATTERN, user.getId(), MAX_COURSES_NUMBER));
        }

        var course = get(courseId);

        enrollRequest = new EnrollRequest()
                .setUser(user)
                .setCourse(course)
                .setIsActive(true)
                .setIsApproved(false);

        return enrollRequestRepository.save(enrollRequest);
    }

    public List<EnrollRequest> getAllEnrollRequestsForCourse(Long courseId, Boolean isActive) {
        if (isNull(isActive)) {
            return enrollRequestRepository.getAllByCourseId(courseId);
        }
        return enrollRequestRepository.getAllByCourseIdAndIsActive(courseId, isActive);
    }

    public UserToCourse approveEnrollRequest(Long enrollRequestId, Boolean isApproved) {
        var enrollRequest = getEnrollRequest(enrollRequestId);

        enrollRequest.setIsActive(false)
                .setIsApproved(isApproved);

        enrollRequestRepository.save(enrollRequest);

        if (!isApproved) {
            return null;
        }

        return enroll(enrollRequest.getCourse().getId(), enrollRequest.getUser().getId());
    }

    public EnrollRequest getUsersLastEnrollRequest(Long userId, Long courseId) {
        return enrollRequestRepository.getLastByCourseIdAndUserId(courseId, userId);
    }

    private EnrollRequest getEnrollRequest(Long enrollRequestId) {
        return enrollRequestRepository.findById(enrollRequestId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENROLL_REQUEST_MISSING_PATTERN, enrollRequestId)));
    }
}
