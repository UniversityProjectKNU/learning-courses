package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.security.SignInDto;
import com.nazar.grynko.learningcourses.dto.security.SignUpDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserSecurityDto;
import com.nazar.grynko.learningcourses.mapper.UserMapper;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.security.JwtProvider;
import com.nazar.grynko.learningcourses.security.JwtUserDetailsService;
import com.nazar.grynko.learningcourses.service.internal.RoleInternalService;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SecurityService {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserInternalService userInternalService;
    private final UserMapper userMapper;
    private final RoleInternalService roleInternalService;

    public SecurityService(JwtUserDetailsService jwtUserDetailsService, JwtProvider jwtProvider,
                           PasswordEncoder passwordEncoder, UserInternalService userInternalService,
                           UserMapper userMapper, RoleInternalService roleInternalService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.userInternalService = userInternalService;
        this.userMapper = userMapper;
        this.roleInternalService = roleInternalService;
    }

    public UserSecurityDto signIn(SignInDto signInDto) {
        var userDetails = jwtUserDetailsService.loadUserByUsername(signInDto.getLogin());

        //TODO make password comparison: !passwordEncoder.matches(signInDto.getPassword(), userDetails.getPassword())
        if (!Objects.equals(signInDto.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Password is incorrect");
        }

        var roleValue = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .get(0);

        return new UserSecurityDto()
                .setLogin(signInDto.getLogin())
                .setToken(jwtProvider.generateToken(userDetails))
                .setRole(RoleType.valueOf(roleValue));
    }

    public UserDto signUp(SignUpDto dto) {
        if (userInternalService.existsUser(dto.getLogin())) {
            throw new IllegalArgumentException(String.format("User with login %s already exists", dto.getLogin()));
        }
        //TODO make password encoding: dto.setPassword(passwordEncoder.encodePassword(dto.getPassword()));
        var user = userMapper.fromDtoSave(dto);
        user.setRole(RoleType.STUDENT);

        user = userInternalService.save(user);

        return userMapper.toDto(user);
    }

}
