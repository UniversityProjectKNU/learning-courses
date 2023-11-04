package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInternalService {

    private final UserToCourseInternalService userToCourseInternalService;
    private final UserRepository userRepository;

    @Autowired
    public UserInternalService(UserToCourseInternalService userToCourseInternalService,
                               UserRepository userRepository) {
        this.userToCourseInternalService = userToCourseInternalService;
        this.userRepository = userRepository;
    }

    public Optional<User> get(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void delete(Long id) {
        User dbUser = userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        userRepository.delete(dbUser);
    }

    public User update(User user) {
        User dbUser = userRepository.findById(user.getId())
                .orElseThrow(IllegalArgumentException::new);
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

    //TODO invalidate token after role is changed
    public User updateRole(RoleType role, Long userId) {
        if (hasUserActiveCourses(userId)) {
            throw new IllegalArgumentException("Cannot update user's roles since user has active courses");
        }

        User user = get(userId)
                .orElseThrow(InvalidPathException::new)
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
}
