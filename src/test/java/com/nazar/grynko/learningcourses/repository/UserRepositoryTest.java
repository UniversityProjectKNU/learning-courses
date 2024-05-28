package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.model.User;
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
public class UserRepositoryTest {

    @Container
    private static final MySQLContainer<?> database = new MySQLContainer<>("mysql:8.0.31")
            .withDatabaseName("learning_courses");

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository repository;

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
    @DisplayName("Given an entity in database. Find entity by login. Should return non null value.")
    void findByLogin_ifExists1() {
        // PREPARE
        var login = "some@gmail.com";
        var password = "password";
        var firstName = "Grynko";
        var lastName = "Nazar";
        var user = new User()
                .setRole(RoleType.STUDENT)
                .setLogin(login)
                .setPassword(password)
                .setLastName(lastName)
                .setFirstName(firstName);

        // ACT
        entityManager.persist(user);
        var entity = repository.findByLogin(login)
                .orElseThrow();

        // VERIFY
        assertNotNull(entity);
        assertEquals(user.getLogin(), login);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getFirstName(), firstName);
        assertEquals(user.getLastName(), lastName);
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given a lot of entities in database. Find entity by login. Should return non null value.")
    void findByLogin_ifExists2() {
        // PREPARE
        var login = "user1@gmail.com";
        // ACT
        var entity = repository.findByLogin(login)
                .orElseThrow();

        // VERIFY
        assertNotNull(entity);
    }

    @Test
    @DisplayName("Given an empty database. Find entity by login. Should return null value.")
    void findByLogin_ifNotExists1() {
        // PREPARE
        var login = "test@gmail.com";

        // ACT
        var actual = repository.findByLogin(login);

        // VERIFY
        assertTrue(actual.isEmpty());
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given a lot of entities in database. Find entity by login. Should return null value.")
    void findByLogin_ifNotExists2() {
        // PREPARE
        var login = "test@gmail.com";

        // ACT
        var actual = repository.findByLogin(login);

        // VERIFY
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Given an entity in database. Check if entity exists by login. Should return true.")
    void existsUserByLogin_ifExists1() {
        // PREPARE
        var login = "some@gmail.com";
        var password = "password";
        var firstName = "Grynko";
        var lastName = "Nazar";
        var user = new User()
                .setRole(RoleType.STUDENT)
                .setLogin(login)
                .setPassword(password)
                .setLastName(lastName)
                .setFirstName(firstName);

        // ACT
        entityManager.persist(user);
        var actual = repository.existsUserByLogin(login);

        // VERIFY
        assertTrue(actual);
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given a lot of entities in database. Check if entity exists by login. Should return true.")
    void existsUserByLogin_ifExists2() {
        // PREPARE
        var login = "user1@gmail.com";
        // ACT
        var actual = repository.existsUserByLogin(login);

        // VERIFY
        assertTrue(actual);
    }

    @Test
    @DisplayName("Given an empty database. Check if entity exists by login. Should return false.")
    void existsUserByLogin_ifNotExists1() {
        // PREPARE
        var login = "test@gmail.com";

        // ACT
        var actual = repository.existsUserByLogin(login);

        // VERIFY
        assertFalse(actual);
    }

    @Test
    @Sql("/sql/user-to-lesson.sql")
    @DisplayName("Given a lot of entities in database. Check if entity exists by login. Should return false.")
    void existsUserByLogin_ifNotExists2() {
        // PREPARE
        var login = "test@gmail.com";

        // ACT
        var actual = repository.existsUserByLogin(login);

        // VERIFY
        assertFalse(actual);
    }

    @Test
    @DisplayName("Given an entity with duplicate login. Should throw an exception.")
    void saveUser_withDuplicateLogin_shouldThrowException() {
        // PREPARE
        var login = "duplicate@gmail.com";
        var user1 = new User()
                .setRole(RoleType.STUDENT)
                .setLogin(login)
                .setPassword("password1")
                .setLastName("LastName1")
                .setFirstName("FirstName1");

        var user2 = new User()
                .setRole(RoleType.STUDENT)
                .setLogin(login)
                .setPassword("password2")
                .setLastName("LastName2")
                .setFirstName("FirstName2");

        // ACT
        entityManager.persist(user1);
        entityManager.flush();

        // VERIFY
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user2));
    }

    @Test
    @DisplayName("Given an entity with null values for required fields. Should throw an exception.")
    void saveUser_withNullValues_shouldThrowException() {
        // PREPARE
        var user = new User();

        // ACT + VERIFY
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }

    @Test
    @DisplayName("Given an entity with null login. Should throw an exception.")
    void saveUser_withNullLogin_shouldThrowException() {
        // PREPARE
        var user = new User()
                .setRole(RoleType.STUDENT)
                .setPassword("password")
                .setLastName("LastName")
                .setFirstName("FirstName");

        // ACT + VERIFY
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }

    @Test
    @DisplayName("Given an entity with null password. Should throw exception.")
    void saveUser_withNullPassword_shouldThrowException() {
        // PREPARE
        var user = new User()
                .setRole(RoleType.STUDENT)
                .setLogin("login")
                .setLastName("LastName")
                .setFirstName("FirstName");

        // ACT + VERIFY
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }

    @Test
    @DisplayName("Given an entity with null first name. Should throw exception.")
    void saveUser_withNullFirstName_shouldThrowException() {
        // PREPARE
        var user = new User()
                .setRole(RoleType.STUDENT)
                .setLogin("login")
                .setPassword("password")
                .setLastName("LastName");

        // ACT + VERIFY
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }

    @Test
    @DisplayName("Given an entity with null last name. Should throw exception.")
    void saveUser_withNullLastName_shouldThrowException() {
        // PREPARE
        var user = new User()
                .setRole(RoleType.STUDENT)
                .setLogin("login")
                .setPassword("password")
                .setFirstName("FirstName");

        // ACT + VERIFY
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }

    @Test
    @DisplayName("Given an entity with null role. Should throw exception.")
    void saveUser_withNullRole_shouldThrowException() {
        // PREPARE
        var user = new User()
                .setLogin("login")
                .setPassword("password")
                .setFirstName("FirstName")
                .setLastName("LastName");

        // ACT + VERIFY
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }
}
