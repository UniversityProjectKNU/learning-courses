package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.model.User;
import org.springframework.stereotype.Service;

@Service
public class RoleInternalService {

    private final UserInternalService userInternalService;
    private final UserToCourseInternalService userToCourseInternalService;

    public RoleInternalService(UserInternalService userInternalService,
                               UserToCourseInternalService userToCourseInternalService) {
        this.userInternalService = userInternalService;
        this.userToCourseInternalService = userToCourseInternalService;
    }

    //TODO invalidate token after role is changed
    public RoleType updateRole(RoleType role, Long userId) {
        if (hasUserActiveCourses(userId)) {
            throw new IllegalArgumentException("Cannot update user's roles since user has active courses");
        }

        User user = userInternalService.get(userId)
                .orElseThrow(InvalidPathException::new)
                .setRole(role);
        user = userInternalService.update(user);

        return user.getRole();
    }

    private boolean hasUserActiveCourses(Long userId) {
        var activeCoursesAmount = userToCourseInternalService
                .getAllByUserId(userId)
                .stream()
                .filter(userToCourse -> !userToCourse.getCourse().getIsFinished())
                .count();

        return activeCoursesAmount > 0;
    }

    public RoleType getUsersRole(Long id) {
        return userInternalService.get(id)
                .orElseThrow(InvalidPathException::new)
                .getRole();
    }

}
