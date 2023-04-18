package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleInternalService {

    private final RoleRepository roleRepository;
    private final UserInternalService userInternalService;

    public RoleInternalService(RoleRepository roleRepository, UserInternalService userInternalService) {
        this.roleRepository = roleRepository;
        this.userInternalService = userInternalService;
    }

    //TODO invalidate token after role is changed
    public Set<Role> updateRoles(Set<Role> roles, Long userId) {
        if (roles == null || roles.size() == 0) throw new IllegalArgumentException();

        User user = userInternalService.get(userId)
                .orElseThrow(InvalidPathException::new)
                .setRoles(roles);

        user = userInternalService.update(user);
        return user.getRoles();
    }

    public Set<Role> getUsersRoles(Long id) {
        return userInternalService.get(id)
                .orElseThrow(InvalidPathException::new)
                .getRoles();
    }

    public Role getByType(RoleType type) {
        return roleRepository.getByType(type);
    }

}
