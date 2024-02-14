package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInternalService {

    private final UserToCourseInternalService userToCourseInternalService;
    private final UserRepository userRepository;

    private static final String USER_ID_MISSING_PATTERN = "User %d doesn't exist";
    private static final String USER_LOGIN_MISSING_PATTERN = "User %s doesn't exist";

    public UserInternalService(UserToCourseInternalService userToCourseInternalService,
                               UserRepository userRepository) {
        this.userToCourseInternalService = userToCourseInternalService;
        this.userRepository = userRepository;
    }

    public User get(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_ID_MISSING_PATTERN, userId)));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_LOGIN_MISSING_PATTERN, login)));
    }

    //todo if student is deleted should we delete their files in S3?
    /*todo improve deleting for instructors throw either deeper logic (what if they have an active course and they are the only instructor / course has an another instructor;
          what would be with feedbacks), or with blocking users logic (that way we can persist instructor but remove an access to the applicaiton)
    */
    public void delete(Long userId) {
        User dbUser = get(userId);
        var role = dbUser.getRole();

        if (role.equals(RoleType.ADMIN)) {
            throw new IllegalStateException("ADMIN user cannot be deleted");
        } else if (role.equals(RoleType.INSTRUCTOR)) {
            var numberOfUserToCourse = userToCourseInternalService.getNumberOfUserToCourse(userId);
            if (numberOfUserToCourse > 0) {
                throw new IllegalStateException("INSTRUCTOR user cannot be deleted if they have already an course interaction");
            }
        }

        userRepository.delete(dbUser);
    }

    public User update(User user) {
        User dbUser = get(user.getId());
        fillNullFields(dbUser, user);

        return userRepository.save(user);
    }

    public boolean existsUser(String login) {
        return userRepository.existsUserByLogin(login);
    }

    private void fillNullFields(User source, User destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getLogin() == null) destination.setLogin(source.getLogin());
        if (destination.getPassword() == null) destination.setPassword(source.getPassword());
        if (destination.getFirstName() == null) destination.setFirstName(source.getFirstName());
        if (destination.getLastName() == null) destination.setLastName(source.getLastName());
        if (destination.getRole() == null) destination.setRole(source.getRole());
    }

    public User save(User entity) {
        return userRepository.save(entity);
    }

    public User updateRole(RoleType role, Long userId) {
        if (hasUserActiveCourses(userId)) {
            throw new IllegalArgumentException("Cannot update user's roles since user has active courses");
        }

        User user = get(userId)
                .setRole(role);
        user = update(user);

        return user;
    }

    private boolean hasUserActiveCourses(Long userId) {
        var activeCoursesAmount = userToCourseInternalService
                .getAllByUserId(userId)
                .stream()
                .filter(userToCourse -> !userToCourse.getCourse().getIsFinished())
                .count();

        return activeCoursesAmount > 0;
    }

    public void throwIfMissingUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_ID_MISSING_PATTERN, userId)));
    }
    public void throwIfMissingUser(String login) {
        userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_LOGIN_MISSING_PATTERN, login)));
    }
}
