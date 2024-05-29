package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.model.UserToCourse;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserToCourseRepositoryTest {

    @Container
    private static final MySQLContainer<?> database = new MySQLContainer<>("mysql:8.0.31")
            .withDatabaseName("learning_courses");

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserToCourseRepository repository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
        registry.add("spring.flyway.url", database::getJdbcUrl);
        registry.add("spring.flyway.user", database::getUsername);
        registry.add("spring.flyway.password", database::getPassword);
    }

    @Test
    @DisplayName("Given a valid UserToCourse entity. When saved. Then should be retrievable.")
    void saveAndRetrieveUserToCourse() {
        // PREPARE
        var user = new User()
                .setLogin("user@gmail.com")
                .setPassword("password")
                .setFirstName("John")
                .setLastName("Doe")
                .setRole(RoleType.STUDENT);
        var course = new Course()
                .setTitle("Course Title")
                .setDescription("Course Description")
                .setIsFinished(false);

        var userToCourse = new UserToCourse()
                .setUser(user)
                .setCourse(course)
                .setMark(85.0f)
                .setIsPassed(true)
                .setFinalFeedback("Excellent");

        // ACT
        entityManager.persist(user);
        entityManager.persist(course);
        var inserted = entityManager.persistFlushFind(userToCourse);
        var actual = repository.findById(inserted.getId()).orElseThrow();

        // VERIFY
        assertNotNull(actual);
        assertEquals(85.0f, actual.getMark());
        assertEquals(true, actual.getIsPassed());
        assertEquals("Excellent", actual.getFinalFeedback());
        assertEquals(user.getId(), actual.getUser().getId());
        assertEquals(course.getId(), actual.getCourse().getId());
    }

    @Test
    @DisplayName("Given an empty database. When searched for non-existing UserToCourse. Then should return empty.")
    void findById_ifNotExists() {
        // PREPARE
        var nonExistentId = 999L;

        // ACT
        var foundUserToCourse = repository.findById(nonExistentId);

        // VERIFY
        assertTrue(foundUserToCourse.isEmpty());
    }

    @Test
    @DisplayName("Given a UserToCourse with null mark. When saved. Then should throw exception.")
    void saveUserToCourse_withNullMark_shouldThrowException() {
        // PREPARE
        var user = new User()
                .setLogin("user@gmail.com")
                .setPassword("password")
                .setFirstName("John")
                .setLastName("Doe")
                .setRole(RoleType.STUDENT);
        var course = new Course()
                .setTitle("Course Title")
                .setDescription("Course Description")
                .setIsFinished(false);

        var userToCourse = new UserToCourse()
                .setUser(user)
                .setCourse(course)
                .setIsPassed(true)
                .setFinalFeedback("Excellent");

        // ACT + VERIFY
        entityManager.persist(user);
        entityManager.persist(course);
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(userToCourse));
    }

    @Test
    @DisplayName("Given a UserToCourse with null isPassed. When saved. Then should throw exception.")
    void saveUserToCourse_withNullIsPassed_shouldThrowException() {
        // PREPARE
        var user = new User()
                .setLogin("user@gmail.com")
                .setPassword("password")
                .setFirstName("John")
                .setLastName("Doe")
                .setRole(RoleType.STUDENT);
        var course = new Course()
                .setTitle("Course Title")
                .setDescription("Course Description")
                .setIsFinished(false);

        var userToCourse = new UserToCourse()
                .setUser(user)
                .setCourse(course)
                .setMark(85.0f)
                .setFinalFeedback("Excellent");

        // ACT + VERIFY
        entityManager.persist(user);
        entityManager.persist(course);
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(userToCourse));
    }

    @Test
    @DisplayName("Given a UserToCourse with null user. When saved. Then should throw exception.")
    void saveUserToCourse_withNullUser_shouldThrowException() {
        // PREPARE
        var course = new Course()
                .setTitle("Course Title")
                .setDescription("Course Description")
                .setIsFinished(false);

        var userToCourse = new UserToCourse()
                .setCourse(course)
                .setMark(85.0f)
                .setIsPassed(true)
                .setFinalFeedback("Excellent");

        // ACT + VERIFY
        entityManager.persist(course);
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(userToCourse));
    }

    @Test
    @DisplayName("Given a UserToCourse with null course. When saved. Then should throw exception.")
    void saveUserToCourse_withNullCourse_shouldThrowException() {
        // PREPARE
        var user = new User()
                .setLogin("user@gmail.com")
                .setPassword("password")
                .setFirstName("John")
                .setLastName("Doe")
                .setRole(RoleType.STUDENT);

        var userToCourse = new UserToCourse()
                .setUser(user)
                .setMark(85.0f)
                .setIsPassed(true)
                .setFinalFeedback("Excellent");

        // ACT + VERIFY
        entityManager.persist(user);
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(userToCourse));
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given a valid userId. When queried. Then should return all UserToCourse entities for the user.")
    void getAllByUserId() {
        // PREPARE
        var userId = 1L;

        // ACT
        var userToCourses = repository.getAllByUserId(userId);

        // VERIFY
        assertNotNull(userToCourses);
        assertFalse(userToCourses.isEmpty());
        userToCourses.forEach(utc -> assertEquals(userId, utc.getUser().getId()));
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given a valid courseId. When queried. Then should return all UserToCourse entities for the course.")
    void getAllByCourseId() {
        // PREPARE
        var courseId = 1L;

        // ACT
        var userToCourses = repository.getAllByCourseId(courseId);

        // VERIFY
        assertNotNull(userToCourses);
        assertFalse(userToCourses.isEmpty());
        userToCourses.forEach(utc -> assertEquals(courseId, utc.getCourse().getId()));
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given valid userId and courseId. When queried. Then should return the corresponding UserToCourse entity.")
    void getByUserIdAndCourseId() {
        // PREPARE
        var userId = 1L;
        var courseId = 1L;

        // ACT
        var userToCourse = repository.getByUserIdAndCourseId(userId, courseId);

        // VERIFY
        assertTrue(userToCourse.isPresent());
        assertEquals(userId, userToCourse.get().getUser().getId());
        assertEquals(courseId, userToCourse.get().getCourse().getId());
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given invalid userId. When queried. Then should return empty list.")
    void getAllByUserId_ifNotExists() {
        // PREPARE
        var userId = 999L;

        // ACT
        var userToCourses = repository.getAllByUserId(userId);

        // VERIFY
        assertNotNull(userToCourses);
        assertTrue(userToCourses.isEmpty());
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given invalid courseId. When queried. Then should return empty list.")
    void getAllByCourseId_ifNotExists() {
        // PREPARE
        var courseId = 999L;

        // ACT
        var userToCourses = repository.getAllByCourseId(courseId);

        // VERIFY
        assertNotNull(userToCourses);
        assertTrue(userToCourses.isEmpty());
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given invalid userId and courseId. When queried. Then should return empty optional.")
    void getByUserIdAndCourseId_ifNotExists() {
        // PREPARE
        var userId = 999L;
        var courseId = 999L;

        // ACT
        var userToCourse = repository.getByUserIdAndCourseId(userId, courseId);

        // VERIFY
        assertTrue(userToCourse.isEmpty());
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given course ID and user ID. When deleteByCourseIdAndUserId is called. Then should delete the entity.")
    void deleteByCourseIdAndUserId() {
        // PREPARE
        var userId = 2L;
        var courseId = 1L;

        // ACT
        var sizeBeforeDelete = repository.findAll().size();
        repository.deleteByCourseIdAndUserId(courseId, userId);
        var userToCourse = repository.getByUserIdAndCourseId(userId, courseId);
        var sizeAfterDelete = repository.findAll().size();

        // VERIFY
        assertTrue(userToCourse.isEmpty());
        assertEquals(sizeBeforeDelete, sizeAfterDelete + 1);
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given non existing course ID and user ID. When deleteByCourseIdAndUserId is called. Then should not delete any entity.")
    void deleteByCourseIdAndUserId_nonExisting() {
        // PREPARE
        var userId = 999L;
        var courseId = 999L;

        // ACT
        var sizeBeforeDelete = repository.findAll().size();
        repository.deleteByCourseIdAndUserId(courseId, userId);
        var userToCourse = repository.getByUserIdAndCourseId(userId, courseId);
        var sizeAfterDelete = repository.findAll().size();

        // VERIFY
        assertTrue(userToCourse.isEmpty());
        assertEquals(sizeBeforeDelete, sizeAfterDelete);
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given user login and course ID. When getByUserLoginAndCourseId is called. Then should return the UserToCourse entity.")
    void getByUserLoginAndCourseId() {
        // PREPARE
        var login = "instructor2@gmail.com";
        var courseId = 1L;

        // ACT
        var userToCourse = repository.getByUserLoginAndCourseId(login, courseId);

        // VERIFY
        assertTrue(userToCourse.isPresent());
        assertEquals(login, userToCourse.get().getUser().getLogin());
        assertEquals(courseId, userToCourse.get().getCourse().getId());
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given non-existing user login and course ID. When getByUserLoginAndCourseId is called. Then should return empty.")
    void getByUserLoginAndCourseId_nonExisting() {
        // PREPARE
        var login = "nonexistent@gmail.com";
        var courseId = 999L;

        // ACT
        var userToCourse = repository.getByUserLoginAndCourseId(login, courseId);

        // VERIFY
        assertTrue(userToCourse.isEmpty());
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given user ID. When getNumberOfUserToCourse is called. Then should return the number of UserToCourse entities.")
    void getNumberOfUserToCourse() {
        // PREPARE
        var userId = 2L;

        // ACT
        var numberOfUserToCourse = repository.getNumberOfUserToCourse(userId);

        // VERIFY
        assertEquals(2, numberOfUserToCourse); // User 2 is associated with 2 courses
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given non-existing user ID. When getNumberOfUserToCourse is called. Then should return zero.")
    void getNumberOfUserToCourse_nonExisting() {
        // PREPARE
        var userId = 999L;

        // ACT
        var numberOfUserToCourse = repository.getNumberOfUserToCourse(userId);

        // VERIFY
        assertEquals(0, numberOfUserToCourse);
    }

}