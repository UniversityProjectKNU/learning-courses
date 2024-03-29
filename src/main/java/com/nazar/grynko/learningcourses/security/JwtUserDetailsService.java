package com.nazar.grynko.learningcourses.security;

import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.service.internal.RoleInternalService;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserInternalService userInternalService;
    private final RoleInternalService roleInternalService;

    public JwtUserDetailsService(UserInternalService userInternalService, RoleInternalService roleInternalService) {
        this.userInternalService = userInternalService;
        this.roleInternalService = roleInternalService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userInternalService
                .getByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with login %s doesn't exist", login)));

        var authorities = roleInternalService
                .getUsersRoles(user.getId())
                .stream()
                .map(Role::getType)
                .map(type -> new SimpleGrantedAuthority(type.toString()))
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

}
