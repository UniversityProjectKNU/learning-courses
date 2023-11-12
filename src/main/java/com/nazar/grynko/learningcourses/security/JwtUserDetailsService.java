package com.nazar.grynko.learningcourses.security;

import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserInternalService userInternalService;

    public JwtUserDetailsService(UserInternalService userInternalService) {
        this.userInternalService = userInternalService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userInternalService
                .getByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with login %s doesn't exist", login)));

        var authorities = Stream.of(user.getRole())
                .map(type -> new SimpleGrantedAuthority(type.toString()))
                .collect(Collectors.toList());

        return new MyUserDetails()
                .setId(user.getId())
                .setUsername(user.getLogin())
                .setPassword(user.getPassword())
                .setAuthorities(authorities);
    }

}
