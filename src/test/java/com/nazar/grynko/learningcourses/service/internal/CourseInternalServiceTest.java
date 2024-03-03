package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.mapper.CourseMapper;
import com.nazar.grynko.learningcourses.model.*;
import com.nazar.grynko.learningcourses.repository.CourseOwnerRepository;
import com.nazar.grynko.learningcourses.repository.CourseRepository;
import com.nazar.grynko.learningcourses.repository.EnrollRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.nazar.grynko.learningcourses.util.MockEntities.mockCourse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseInternalServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseTemplateInternalService courseTemplateInternalService;
    @Mock
    private UserInternalService userInternalService;
    @Mock
    private ChapterInternalService chapterInternalService;
    @Mock
    private UserToCourseInternalService userToCourseInternalService;
    @Mock
    private CourseOwnerRepository courseOwnerRepository;
    @Mock
    private LessonTemplateInternalService lessonTemplateInternalService;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private UserToLessonInternalService userToLessonInternalService;
    @Mock
    private LessonInternalService lessonInternalService;
    @Mock
    private EnrollRequestRepository enrollRequestRepository;

    @InjectMocks
    private CourseInternalService sut;

    @Test
    public void shouldReturnCourseWhenExists() {
        // PREPARE
        Long courseId = 1L;
        var expected = mockCourse();

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(expected));

        // ACT
        var actual = sut.get(courseId);

        // VERIFY
        assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowExceptionWhenCourseNotFound() {
        // PREPARE
        Long courseId = 100L;

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // ACT & VERIFY
        assertThrows(EntityNotFoundException.class, () -> sut.get(courseId));
    }

    @Test
    public void shouldReturnAllCourses() {
        // PREPARE
        List<Course> expectedCourses = Arrays.asList(
                new Course(1L, "Test Course 1", "Description 1", false),
                new Course(2L, "Test Course 2", "Description 2", true)
        );

        // MOCKING
        when(courseRepository.findAll()).thenReturn(expectedCourses);

        // ACT
        List<Course> actualCourses = sut.getAll();

        // VERIFY
        assertEquals(expectedCourses, actualCourses);
    }

    @Test
    public void shouldReturnEmptyListWhenNoCourses() {
        // MOCKING
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        // ACT
        List<Course> actualCourses = sut.getAll();

        // VERIFY
        assertTrue(actualCourses.isEmpty());
    }

    @Test
    public void shouldDeleteCourseWhenExists() {
        // PREPARE
        Long courseId = 1L;

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course()));
        doNothing().when(courseRepository).delete(any(Course.class));

        // ACT
        sut.delete(courseId);

        // VERIFY
        verify(courseRepository).findById(courseId);
        verify(courseRepository).delete(any(Course.class));
    }

    @Test
    public void shouldThrowExceptionWhenDeleteAndCourseNotFound() {
        // PREPARE
        Long courseId = 100L;

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // ACT & VERIFY
        assertThrows(EntityNotFoundException.class, () -> sut.delete(courseId));
        verify(courseRepository, never()).delete(any(Course.class));
    }


    @Test
    public void shouldCreateCourseWhenAllDataIsValid() {
        // PREPARE
        Long courseTemplateId = 1L;
        String login = "test_user";
        long expectedLessonsNumber = 10;

        // MOCKING
        when(courseTemplateInternalService.get(courseTemplateId)).thenReturn(new CourseTemplate());
        when(userInternalService.getByLogin(login)).thenReturn(new User());
        when(courseRepository.save(any(Course.class))).thenReturn(new Course());
        when(courseMapper.fromTemplate(any(CourseTemplate.class))).thenReturn(new Course());
        when(lessonTemplateInternalService.getNumberOfLessonsForCourseTemplate(courseTemplateId)).thenReturn(expectedLessonsNumber);

        // ACT
        sut.create(courseTemplateId, login);

        // VERIFY
        verify(courseTemplateInternalService).get(courseTemplateId);
        verify(userInternalService).getByLogin(login);
        verify(courseRepository).save(any(Course.class));
        verify(userToCourseInternalService).save(any(UserToCourse.class));
        verify(courseOwnerRepository).save(any(CourseOwner.class));
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCourseTemplateNotFound() {
        // PREPARE
        Long courseTemplateId = 100L;
        String login = "test_user";
        long expectedLessonsNumber = 10;

        // MOCKING
        when(lessonTemplateInternalService.getNumberOfLessonsForCourseTemplate(courseTemplateId)).thenReturn(expectedLessonsNumber);
        when(courseTemplateInternalService.get(courseTemplateId)).thenThrow(EntityNotFoundException.class);

        // ACT & VERIFY
        assertThrows(EntityNotFoundException.class, () -> sut.create(courseTemplateId, login));
        verify(lessonTemplateInternalService).getNumberOfLessonsForCourseTemplate(any(Long.class));
        verify(courseTemplateInternalService).get(courseTemplateId);
        verify(userInternalService, never()).getByLogin(login);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenUserNotFound() {
        // PREPARE
        Long courseTemplateId = 1L;
        String login = "nonexistent_user";
        long expectedLessonsNumber = 10;

        // MOCKING
        when(lessonTemplateInternalService.getNumberOfLessonsForCourseTemplate(courseTemplateId)).thenReturn(expectedLessonsNumber);
        when(courseTemplateInternalService.get(courseTemplateId)).thenReturn(new CourseTemplate());
        when(userInternalService.getByLogin(login)).thenThrow(EntityNotFoundException.class);

        // ACT & VERIFY
        assertThrows(EntityNotFoundException.class, () -> sut.create(courseTemplateId, login));
        verify(lessonTemplateInternalService).getNumberOfLessonsForCourseTemplate(any(Long.class));
        verify(courseTemplateInternalService).get(courseTemplateId);
        verify(userInternalService).getByLogin(login);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenNotEnoughLessons() {
        // PREPARE
        Long courseTemplateId = 1L;
        String login = "test_user";
        long expectedLessonsNumber = 1;

        // MOCKING
        when(lessonTemplateInternalService.getNumberOfLessonsForCourseTemplate(courseTemplateId)).thenReturn(expectedLessonsNumber);

        // ACT & VERIFY
        assertThrows(IllegalStateException.class, () -> sut.create(courseTemplateId, login));
        verify(lessonTemplateInternalService).getNumberOfLessonsForCourseTemplate(any(Long.class));
        verify(courseTemplateInternalService, never()).get(courseTemplateId);
    }

    @Test
    public void shouldFinishCourseWhenExistsAndNotFinished() {
        // PREPARE
        Long courseId = 1L;
        var course = new Course().setId(courseId).setIsFinished(false);

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(userToLessonInternalService).setIsPassedForLessonsInCourse(courseId);
        doNothing().when(chapterInternalService).finish(courseId);
        doNothing().when(userToCourseInternalService).finish(courseId);

        // ACT
        Course actualCourse = sut.finish(courseId);

        // VERIFY
        verify(courseRepository).findById(courseId);
        verify(courseRepository).save(any(Course.class));
        verify(userToLessonInternalService).setIsPassedForLessonsInCourse(courseId);
        verify(chapterInternalService).finish(courseId);
        verify(userToCourseInternalService).finish(courseId);

        // Check that the course is finished
        assertTrue(actualCourse.getIsFinished());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCourseNotFound() {
        // PREPARE
        Long courseId = 100L;

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // ACT & VERIFY
        assertThrows(EntityNotFoundException.class, () -> sut.finish(courseId));
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenCourseAlreadyFinished() {
        // PREPARE
        Long courseId = 1L;

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course().setId(courseId).setIsFinished(true)));

        // ACT & VERIFY
        assertThrows(IllegalStateException.class, () -> sut.finish(courseId));
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    public void shouldUpdateCourseWhenExists() {
        // PREPARE
        Long courseId = 1L;
        String newTitle = "New title";
        String newDescription = "New description";

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course().setId(courseId)));
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArguments()[0]);

        // ACT
        Course courseToUpdate = new Course().setId(courseId).setTitle(newTitle).setDescription(newDescription);
        Course actualCourse = sut.update(courseToUpdate);

        // VERIFY
        verify(courseRepository).findById(courseId);
        verify(courseRepository).save(any(Course.class));

        // Check that the course was updated
        assertEquals(newTitle, actualCourse.getTitle());
        assertEquals(newDescription, actualCourse.getDescription());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCourseNotFound1() {
        // PREPARE
        Long courseId = 100L;

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // ACT & VERIFY
        assertThrows(EntityNotFoundException.class, () -> sut.update(new Course().setId(courseId)));
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    public void shouldEnrollInstructorToCourse() {
        // PREPARE
        Long courseId = 1L;
        Long userId = 1L;
        RoleType role = RoleType.INSTRUCTOR;

        // MOCKING
        when(userInternalService.get(userId)).thenReturn(new User().setId(userId).setRole(role));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course().setId(courseId)));
        when(userToCourseInternalService.existsByUserIdAndCourseId(userId, courseId)).thenReturn(false);
        when(userToCourseInternalService.save(any(UserToCourse.class))).thenAnswer(i -> i.getArguments()[0]);

        // ACT
        UserToCourse actualUserToCourse = sut.enroll(courseId, userId);

        // VERIFY
        verify(userInternalService).get(userId);
        verify(courseRepository).findById(courseId);
        verify(userToCourseInternalService).existsByUserIdAndCourseId(userId, courseId);
        verify(userToCourseInternalService).save(any(UserToCourse.class));

        // Check the saved entity
        assertEquals(userId, actualUserToCourse.getUser().getId());
        assertEquals(courseId, actualUserToCourse.getCourse().getId());
        assertFalse(actualUserToCourse.getIsPassed());
        assertEquals(0f, actualUserToCourse.getMark(), 0.001);
    }

    @Test
    public void shouldEnrollStudentToCourseWhenLimitNotExceeded() {
        // PREPARE
        Long courseId = 1L;
        Long userId = 1L;
        RoleType role = RoleType.STUDENT;
        int expectedActiveCourses = 3;

        // MOCKING
        when(userInternalService.get(userId)).thenReturn(new User().setId(userId).setRole(role));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course().setId(courseId)));
        when(userToCourseInternalService.existsByUserIdAndCourseId(userId, courseId)).thenReturn(false);
        when(userToCourseInternalService.getAllByUserId(userId)).thenReturn(
                Arrays.asList(
                        new UserToCourse().setCourse(new Course().setIsFinished(true)),
                        new UserToCourse().setCourse(new Course().setIsFinished(true)),
                        new UserToCourse().setCourse(new Course().setIsFinished(false))
                )
        );
        when(userToCourseInternalService.save(any(UserToCourse.class))).thenAnswer(i -> i.getArguments()[0]);
        when(lessonInternalService.enroll(any(User.class), anyLong())).thenReturn(null);

        // ACT
        UserToCourse actualUserToCourse = sut.enroll(courseId, userId);

        // VERIFY
        verify(userInternalService).get(userId);
        verify(courseRepository).findById(courseId);
        verify(userToCourseInternalService).existsByUserIdAndCourseId(userId, courseId);
        verify(userToCourseInternalService).getAllByUserId(userId);
        verify(userToCourseInternalService).save(any(UserToCourse.class));
        verify(lessonInternalService).enroll(any(User.class), anyLong());

        // Check the saved entity
        assertEquals(userId, actualUserToCourse.getUser().getId());
        assertEquals(courseId, actualUserToCourse.getCourse().getId());
        assertFalse(actualUserToCourse.getIsPassed());
        assertEquals(0f, actualUserToCourse.getMark(), 0.001);
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenUserAlreadyEnrolled() {
        // PREPARE
        Long courseId = 1L;
        Long userId = 1L;

        // MOCKING
        when(userInternalService.get(userId)).thenReturn(new User().setId(userId).setRole(RoleType.STUDENT));
        when(userToCourseInternalService.existsByUserIdAndCourseId(userId, courseId)).thenReturn(true);

        // ACT & VERIFY
        assertThrows(IllegalStateException.class, () -> sut.enroll(courseId, userId));
        verify(userInternalService).get(userId);
        verify(userToCourseInternalService).existsByUserIdAndCourseId(userId, courseId);
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenUserIsAdmin() {
        // PREPARE
        Long courseId = 1L;
        Long userId = 1L;
        RoleType role = RoleType.ADMIN;

        // MOCKING
        when(userInternalService.get(userId)).thenReturn(new User().setId(userId).setRole(role));

        // ACT & VERIFY
        assertThrows(IllegalStateException.class, () -> sut.enroll(courseId, userId));
        verify(userInternalService).get(userId);
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenStudentExceedsCoursesLimit() {
        // PREPARE
        Long courseId = 1L;
        Long userId = 1L;
        RoleType role = RoleType.STUDENT;
        int expectedActiveCourses = 10;

        // MOCKING
        when(userInternalService.get(userId)).thenReturn(new User().setId(userId).setRole(role));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course().setId(courseId)));
        when(userToCourseInternalService.existsByUserIdAndCourseId(userId, courseId)).thenReturn(false);
        when(userToCourseInternalService.getAllByUserId(userId)).thenReturn(
                Arrays.asList(
                        new UserToCourse().setCourse(new Course().setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setIsFinished(false))
                )
        );

        // ACT & VERIFY
        assertThrows(IllegalStateException.class, () -> sut.enroll(courseId, userId));
        verify(userInternalService).get(userId);
        verify(courseRepository).findById(any());
        verify(userToCourseInternalService).existsByUserIdAndCourseId(userId, courseId);
        verify(userToCourseInternalService).getAllByUserId(userId);
    }

    @Test
    public void shouldReturnAllUsersCoursesWhenIsActiveIsNullAndUserHasCourses() {
        // PREPARE
        String login = "test_user";
        Long userId = 1L;
        int expectedCoursesNumber = 3;

        // MOCKING
        when(userInternalService.getByLogin(login)).thenReturn(new User().setId(userId));
        when(userToCourseInternalService.getAllByUserId(userId)).thenReturn(
                Arrays.asList(
                        new UserToCourse().setCourse(new Course().setId(1L).setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setId(2L).setIsFinished(true)),
                        new UserToCourse().setCourse(new Course().setId(3L).setIsFinished(false))
                )
        );

        // ACT
        List<Course> actualCourses = sut.getAllUsersCourses(login, null);

        // VERIFY
        verify(userInternalService).getByLogin(login);
        verify(userToCourseInternalService).getAllByUserId(userId);

        // Check the number of courses
        assertEquals(expectedCoursesNumber, actualCourses.size());
    }

    @Test
    public void shouldReturnEmptyListWhenIsActiveIsNullAndUserHasNoCourses() {
        // PREPARE
        String login = "test_user";
        Long userId = 1L;

        // MOCKING
        when(userInternalService.getByLogin(login)).thenReturn(new User().setId(userId));
        when(userToCourseInternalService.getAllByUserId(userId)).thenReturn(Collections.emptyList());

        // ACT
        List<Course> actualCourses = sut.getAllUsersCourses(login, null);

        // VERIFY
        verify(userInternalService).getByLogin(login);
        verify(userToCourseInternalService).getAllByUserId(userId);

        // Check the number of courses
        assertEquals(0, actualCourses.size());
    }

    @Test
    public void shouldReturnOnlyFinishedCoursesWhenIsActiveIsTrue() {
        // PREPARE
        String login = "test_user";
        Long userId = 1L;
        int expectedCoursesNumber = 1;

        // MOCKING
        when(userInternalService.getByLogin(login)).thenReturn(new User().setId(userId));
        when(userToCourseInternalService.getAllByUserId(userId)).thenReturn(
                Arrays.asList(
                        new UserToCourse().setCourse(new Course().setId(1L).setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setId(2L).setIsFinished(true)),
                        new UserToCourse().setCourse(new Course().setId(3L).setIsFinished(true))
                )
        );

        // ACT
        List<Course> actualCourses = sut.getAllUsersCourses(login, true);

        // VERIFY
        verify(userInternalService).getByLogin(login);
        verify(userToCourseInternalService).getAllByUserId(userId);

        // Check the number of courses and that all courses are active
        assertEquals(expectedCoursesNumber, actualCourses.size());
        for (Course course : actualCourses) {
            assertFalse(course.getIsFinished());
        }
    }

    @Test
    public void shouldReturnEmptyListWhenIsActiveIsTrueAndUserHasNoFinishedCourses() {
        // PREPARE
        String login = "test_user";
        Long userId = 1L;

        // MOCKING
        when(userInternalService.getByLogin(login)).thenReturn(new User().setId(userId));
        when(userToCourseInternalService.getAllByUserId(userId)).thenReturn(
                Arrays.asList(
                        new UserToCourse().setCourse(new Course().setId(1L).setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setId(2L).setIsFinished(false))
                )
        );

        // ACT
        List<Course> actualCourses = sut.getAllUsersCourses(login, true);

        // VERIFY
        verify(userInternalService).getByLogin(login);
        verify(userToCourseInternalService).getAllByUserId(userId);

        // Check the number of courses
        assertEquals(2, actualCourses.size());
    }

    @Test
    public void shouldNotThrowExceptionWhenCourseExists() {
        // PREPARE
        Long courseId = 1L;

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course()));

        // ACT & VERIFY
        sut.throwIfMissingCourse(courseId);
        verify(courseRepository).findById(courseId);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCourseNotFound2() {
        // PREPARE
        Long courseId = 100L;

        // MOCKING
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // ACT & VERIFY
        assertThrows(EntityNotFoundException.class, () -> sut.throwIfMissingCourse(courseId));
        verify(courseRepository).findById(courseId);
    }

    @Test
    public void shouldSendEnrollRequestWhenUserIsInstructor() {
        // PREPARE
        Long courseId = 1L;
        String login = "test_user";
        RoleType role = RoleType.INSTRUCTOR;

        // MOCKING
        when(userToCourseInternalService.existsByLoginAndCourseId(login, courseId)).thenReturn(false);
        when(enrollRequestRepository.getByCourseIdAndUserLoginAndIsActiveTrue(courseId, login)).thenReturn(null);
        when(userInternalService.getByLogin(login)).thenReturn(new User().setId(1L).setRole(role));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course().setId(courseId)));

        // ACT
        EnrollRequest actualEnrollRequest = sut.sendEnrollRequest(courseId, login);

        // VERIFY
        verify(userToCourseInternalService).existsByLoginAndCourseId(login, courseId);
        verify(enrollRequestRepository).getByCourseIdAndUserLoginAndIsActiveTrue(courseId, login);
        verify(userInternalService).getByLogin(login);
        verify(courseRepository).findById(courseId);
        verify(userToCourseInternalService, never()).getAllByUserId(anyLong());
        verify(enrollRequestRepository).save(any(EnrollRequest.class));
    }

    @Test
    public void shouldSendEnrollRequestWhenUserIsStudentAndHasNotExceededCoursesLimit() {
        // PREPARE
        Long courseId = 1L;
        String login = "test_user";
        RoleType role = RoleType.STUDENT;
        int expectedActiveCourses = 3;

        // MOCKING
        when(userToCourseInternalService.existsByLoginAndCourseId(login, courseId)).thenReturn(false);
        when(enrollRequestRepository.getByCourseIdAndUserLoginAndIsActiveTrue(courseId, login)).thenReturn(null);
        when(userInternalService.getByLogin(login)).thenReturn(new User().setId(1L).setRole(role));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course().setId(courseId)));
        when(userToCourseInternalService.getAllByUserId(anyLong())).thenReturn(
                Arrays.asList(
                        new UserToCourse().setCourse(new Course().setIsFinished(true)),
                        new UserToCourse().setCourse(new Course().setIsFinished(true)),
                        new UserToCourse().setCourse(new Course().setIsFinished(false))
                )
        );
        when(enrollRequestRepository.save(any(EnrollRequest.class))).thenAnswer(i -> i.getArguments()[0]);

        // ACT
        EnrollRequest actualEnrollRequest = sut.sendEnrollRequest(courseId, login);

        // VERIFY
        verify(userToCourseInternalService).existsByLoginAndCourseId(login, courseId);
        verify(enrollRequestRepository).getByCourseIdAndUserLoginAndIsActiveTrue(courseId, login);
        verify(userInternalService).getByLogin(login);
        verify(courseRepository).findById(courseId);
        verify(userToCourseInternalService).getAllByUserId(anyLong());
        verify(enrollRequestRepository).save(any(EnrollRequest.class));
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenUserIsAlreadyEnrolled() {
        // PREPARE
        Long courseId = 1L;
        String login = "test_user";

        // MOCKING
        when(userToCourseInternalService.existsByLoginAndCourseId(login, courseId)).thenReturn(true);

        // ACT & VERIFY
        assertThrows(IllegalStateException.class, () -> sut.sendEnrollRequest(courseId, login));
        verify(userToCourseInternalService).existsByLoginAndCourseId(login, courseId);
        verify(enrollRequestRepository, never()).getByCourseIdAndUserLoginAndIsActiveTrue(anyLong(), anyString());
        verify(userInternalService, never()).getByLogin(anyString());
        verify(courseRepository, never()).findById(courseId);
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenUserHasActiveEnrollRequest() {
        // PREPARE
        Long courseId = 1L;
        String login = "test_user";
        EnrollRequest existingEnrollRequest = new EnrollRequest();

        // MOCKING
        when(userToCourseInternalService.existsByLoginAndCourseId(login, courseId)).thenReturn(false);
        when(enrollRequestRepository.getByCourseIdAndUserLoginAndIsActiveTrue(courseId, login)).thenReturn(existingEnrollRequest);

        // ACT & VERIFY
        assertThrows(IllegalStateException.class, () -> sut.sendEnrollRequest(courseId, login));
        verify(userToCourseInternalService).existsByLoginAndCourseId(login, courseId);
        verify(enrollRequestRepository).getByCourseIdAndUserLoginAndIsActiveTrue(courseId, login);
        verify(userInternalService, never()).getByLogin(anyString());
        verify(courseRepository, never()).findById(courseId);
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenUserIsStudentAndExceedsCoursesLimit() {
        // PREPARE
        Long courseId = 1L;
        String login = "test_user";
        RoleType role = RoleType.STUDENT;
        int expectedActiveCourses = 10;

        // MOCKING
        when(userToCourseInternalService.existsByLoginAndCourseId(login, courseId)).thenReturn(false);
        when(enrollRequestRepository.getByCourseIdAndUserLoginAndIsActiveTrue(courseId, login)).thenReturn(null);
        when(userInternalService.getByLogin(login)).thenReturn(new User().setId(1L).setRole(role));
        when(userToCourseInternalService.getAllByUserId(anyLong())).thenReturn(
                Arrays.asList(
                        new UserToCourse().setCourse(new Course().setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setIsFinished(false)),
                        new UserToCourse().setCourse(new Course().setIsFinished(false))
                )
        );

        // ACT & VERIFY
        assertThrows(IllegalStateException.class, () -> sut.sendEnrollRequest(courseId, login));
        verify(userToCourseInternalService).existsByLoginAndCourseId(login, courseId);
        verify(enrollRequestRepository).getByCourseIdAndUserLoginAndIsActiveTrue(courseId, login);
        verify(userInternalService).getByLogin(login);
        verify(userToCourseInternalService).getAllByUserId(anyLong());
    }

    @Test
    public void shouldReturnAllEnrollRequestsWhenIsActiveIsNullAndResultIsNotEmpty() {
        // PREPARE
        Long courseId = 1L;
        int expectedEnrollRequestsNumber = 3;

        // MOCKING
        when(enrollRequestRepository.getAllByCourseId(courseId)).thenReturn(
                Arrays.asList(
                        new EnrollRequest().setId(1L).setCourse(new Course().setId(courseId)).setIsActive(true),
                        new EnrollRequest().setId(2L).setCourse(new Course().setId(courseId)).setIsActive(false),
                        new EnrollRequest().setId(3L).setCourse(new Course().setId(courseId)).setIsActive(true)
                )
        );

        // ACT
        List<EnrollRequest> actualEnrollRequests = sut.getAllEnrollRequestsForCourse(courseId, null);

        // VERIFY
        verify(enrollRequestRepository).getAllByCourseId(courseId);

        // Check the number of enroll requests
        assertEquals(expectedEnrollRequestsNumber, actualEnrollRequests.size());
    }

    @Test
    public void shouldReturnEmptyListWhenIsActiveIsNullAndResultIsEmpty() {
        // PREPARE
        Long courseId = 1L;

        // MOCKING
        when(enrollRequestRepository.getAllByCourseId(courseId)).thenReturn(Collections.emptyList());

        // ACT
        List<EnrollRequest> actualEnrollRequests = sut.getAllEnrollRequestsForCourse(courseId, null);

        // VERIFY
        verify(enrollRequestRepository).getAllByCourseId(courseId);

        // Check the number of enroll requests
        assertEquals(0, actualEnrollRequests.size());
    }

    @Test
    public void shouldReturnOnlyActiveEnrollRequestsWhenIsActiveIsTrueAndResultIsNotEmpty() {
        // PREPARE
        Long courseId = 1L;
        int expectedEnrollRequestsNumber = 2;

        // MOCKING
        when(enrollRequestRepository.getAllByCourseIdAndIsActive(courseId, true)).thenReturn(
                Arrays.asList(
                        new EnrollRequest().setId(1L).setCourse(new Course().setId(courseId)).setIsActive(true),
                        new EnrollRequest().setId(2L).setCourse(new Course().setId(courseId)).setIsActive(true)
                )
        );

        // ACT
        List<EnrollRequest> actualEnrollRequests = sut.getAllEnrollRequestsForCourse(courseId, true);

        // VERIFY
        verify(enrollRequestRepository).getAllByCourseIdAndIsActive(courseId, true);

        // Check the number of enroll requests and that all requests are active
        assertEquals(expectedEnrollRequestsNumber, actualEnrollRequests.size());
        for (EnrollRequest request : actualEnrollRequests) {
            assertTrue(request.getIsActive());
        }
    }

    @Test
    public void shouldReturnEmptyListWhenIsActiveIsTrueAndResultIsEmpty() {
        // PREPARE
        Long courseId = 1L;

        // MOCKING
        when(enrollRequestRepository.getAllByCourseIdAndIsActive(courseId, true)).thenReturn(Collections.emptyList());

        // ACT
        List<EnrollRequest> actualEnrollRequests = sut.getAllEnrollRequestsForCourse(courseId, true);

        // VERIFY
        verify(enrollRequestRepository).getAllByCourseIdAndIsActive(courseId, true);

        // Check the number of enroll requests
        assertEquals(0, actualEnrollRequests.size());
    }

    @Test
    public void shouldReturnOnlyFinishedEnrollRequestsWhenIsActiveIsFalseAndResultIsNotEmpty() {
        // PREPARE
        Long courseId = 1L;
        int expectedEnrollRequestsNumber = 1;

        // MOCKING
        when(enrollRequestRepository.getAllByCourseIdAndIsActive(courseId, false)).thenReturn(
                List.of(
                        new EnrollRequest().setId(1L).setCourse(new Course().setId(courseId)).setIsActive(false)
                )
        );

        // ACT
        List<EnrollRequest> actualEnrollRequests = sut.getAllEnrollRequestsForCourse(courseId, false);

        // VERIFY
        verify(enrollRequestRepository).getAllByCourseIdAndIsActive(courseId, false);

        // Check the number of enroll requests and that all requests are active
        assertEquals(expectedEnrollRequestsNumber, actualEnrollRequests.size());
        for (EnrollRequest request : actualEnrollRequests) {
            assertFalse(request.getIsActive());
        }
    }

/*    @Test
    public void shouldApproveEnrollRequestAndEnrollUserWhenIsApprovedIsTrue() {
        // PREPARE
        Long enrollRequestId = 1L;
        Long courseId = 2L;
        Long userId = 3L;
        String login = "test_user";

        // MOCKING
        when(enrollRequestRepository.findById(enrollRequestId)).thenReturn(
                Optional.of(
                        new EnrollRequest().setId(enrollRequestId).setCourse(new Course().setId(courseId)).setUser(new User().setId(userId).setLogin(login)).setIsActive(true)
                )
        );
        doNothing(sut).thenReturn(new UserToCourse().setCourse(new Course().setId(courseId)).setUser(new User().setId(userId)));

        // ACT
        UserToCourse actualUserToCourse = sut.approveEnrollRequest(enrollRequestId, true);

        // VERIFY
        verify(enrollRequestRepository).findById(enrollRequestId);
        verify(enrollRequestRepository).save(any(EnrollRequest.class));
        verify(sut.enroll(courseId, userId));

        // Check the saved entity and returned value
        assertEquals(courseId, actualUserToCourse.getCourse().getId());
        assertEquals(login, actualUserToCourse.getUser().getLogin());
    }*/

    @Test
    public void shouldNotApproveEnrollRequestAndNotEnrollUserWhenIsApprovedIsFalse() {
        // PREPARE
        Long enrollRequestId = 1L;

        // MOCKING
        when(enrollRequestRepository.findById(enrollRequestId)).thenReturn(
                Optional.of(
                new EnrollRequest().setId(enrollRequestId).setIsActive(true)
        ));

        // ACT
        UserToCourse actualUserToCourse = sut.approveEnrollRequest(enrollRequestId, false);

        // VERIFY
        verify(enrollRequestRepository).findById(enrollRequestId);
        verify(enrollRequestRepository).save(any(EnrollRequest.class));

        // Check the saved entity and returned value
        assertNull(actualUserToCourse);
    }

    @Test
    public void shouldReturnUsersLastEnrollRequestWhenItExists() {
        // PREPARE
        Long userId = 1L;
        Long courseId = 2L;

        // MOCKING
        when(enrollRequestRepository.getLastByCourseIdAndUserId(courseId, userId)).thenReturn(
                new EnrollRequest().setId(1L).setCourse(new Course().setId(courseId)).setUser(new User().setId(userId)).setIsActive(true)
        );

        // ACT
        EnrollRequest actualEnrollRequest = sut.getUsersLastEnrollRequest(userId, courseId);

        // VERIFY
        verify(enrollRequestRepository).getLastByCourseIdAndUserId(courseId, userId);

        // Check the returned value
        assertNotNull(actualEnrollRequest);
        assertEquals(courseId, actualEnrollRequest.getCourse().getId());
        assertEquals(userId, actualEnrollRequest.getUser().getId());
        assertTrue(actualEnrollRequest.getIsActive());
    }

    @Test
    public void shouldReturnNullWhenUsersLastEnrollRequestNotFound() {
        // PREPARE
        Long userId = 1L;
        Long courseId = 2L;

        // MOCKING
        when(enrollRequestRepository.getLastByCourseIdAndUserId(courseId, userId)).thenReturn(null);

        // ACT
        EnrollRequest actualEnrollRequest = sut.getUsersLastEnrollRequest(userId, courseId);

        // VERIFY
        verify(enrollRequestRepository).getLastByCourseIdAndUserId(courseId, userId);

        // Check the returned value
        assertNull(actualEnrollRequest);
    }

}
