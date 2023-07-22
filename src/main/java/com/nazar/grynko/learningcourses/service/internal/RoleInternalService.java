package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleInternalService {

    private final RoleRepository roleRepository;
    private final UserInternalService userInternalService;
    private final UserToCourseInternalService userToCourseInternalService;

    public RoleInternalService(RoleRepository roleRepository,
                               UserInternalService userInternalService,
                               UserToCourseInternalService userToCourseInternalService) {
        this.roleRepository = roleRepository;
        this.userInternalService = userInternalService;
        this.userToCourseInternalService = userToCourseInternalService;
    }

    //TODO invalidate token after role is changed
    public Set<Role> updateRoles(Set<Role> roles, Long userId) {
        if (roles.size() == 0) {
            throw new IllegalArgumentException("Cannot update user's roles to empty set of roles");
        }
        else if (hasUserActiveCourses(userId)) {
            throw new IllegalArgumentException("Cannot update user's roles since user has active courses");
        }
        else if (getTypes(userId).contains(RoleType.ADMIN)) {
            throw new IllegalArgumentException("Cannot update admin`s roles");
        }

        User user = userInternalService.get(userId)
                .orElseThrow(InvalidPathException::new)
                .setRoles(roles);
        user = userInternalService.update(user);

        return user.getRoles();
    }

    private boolean hasUserActiveCourses(Long userId) {
        var activeCoursesAmount = userToCourseInternalService
                .getAllByUserId(userId)
                .stream()
                .filter(userToCourse -> !userToCourse.getCourse().getIsFinished())
                .count();

        return activeCoursesAmount > 0;
    }

    public Set<Role> getUsersRoles(Long id) {
        return userInternalService.get(id)
                .orElseThrow(InvalidPathException::new)
                .getRoles();
    }

    private List<RoleType> getTypes(Long id) {
        return getUsersRoles(id)
                .stream()
                .map(Role::getType)
                .collect(Collectors.toList());
    }

    public Role getByType(RoleType type) {
        return roleRepository.getByType(type);
    }

}
