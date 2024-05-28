package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Course;
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
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CourseRepositoryTest {

    @Container
    private static final MySQLContainer<?> database = new MySQLContainer<>("mysql:8.0.31")
            .withDatabaseName("learning_courses");

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CourseRepository repository;

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
    @DisplayName("Given a valid entity. When saved. Then should be retrievable.")
    void saveAndRetrieveCourse() {
        // PREPARE
        var title = "Test Course";
        var description = "Test Description";
        var isFinished = false;
        var course = new Course()
                .setTitle(title)
                .setDescription(description)
                .setIsFinished(isFinished);

        // ACT
        var inserted = entityManager.persistFlushFind(course);
        var actual = repository.findById(inserted.getId()).orElseThrow();

        // VERIFY
        assertNotNull(actual);
        assertEquals(title, actual.getTitle());
        assertEquals(description, actual.getDescription());
        assertEquals(isFinished, actual.getIsFinished());
    }

    @Test
    @DisplayName("Given an empty database. When searched for non-existing course. Then should return empty.")
    void findById_ifNotExists() {
        // PREPARE
        var nonExistentId = 999L;

        // ACT
        var foundCourse = repository.findById(nonExistentId);

        // VERIFY
        assertTrue(foundCourse.isEmpty());
    }

    @Test
    @DisplayName("Given a course with null title. When saved. Then should throw exception.")
    void saveCourse_withNullTitle_shouldThrowException() {
        // PREPARE
        var course = new Course()
                .setDescription("Test Description")
                .setIsFinished(false);

        // ACT + VERIFY
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(course));
    }

    @Test
    @DisplayName("Given a course with null description. When saved. Then should throw exception.")
    void saveCourse_withNullDescription_shouldThrowException() {
        // PREPARE
        var course = new Course()
                .setTitle("Test Title")
                .setIsFinished(false);

        // ACT + VERIFY
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(course));
    }

    @Test
    @DisplayName("Given a course with null isFinished. When saved. Then should throw exception.")
    void saveCourse_withNullIsFinished_shouldThrowException() {
        // PREPARE
        var course = new Course()
                .setTitle("Test Title")
                .setDescription("Test Description");

        // ACT + VERIFY
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(course));
    }
}
