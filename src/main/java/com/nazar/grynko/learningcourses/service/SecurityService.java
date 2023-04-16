package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.security.SignInDto;
import com.nazar.grynko.learningcourses.dto.security.SignUpDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.mapper.UserMapper;
import com.nazar.grynko.learningcourses.security.JwtProvider;
import com.nazar.grynko.learningcourses.security.JwtUserDetailsService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SecurityService {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserMapper userMapper;

    public SecurityService(JwtUserDetailsService jwtUserDetailsService, JwtProvider jwtProvider,
                           PasswordEncoder passwordEncoder, UserService userService, UserMapper userMapper) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public String signIn(SignInDto signInDto) {
        var userDetails = jwtUserDetailsService.loadUserByUsername(signInDto.getLogin());

        //TODO make password comparison: !passwordEncoder.matches(signInDto.getPassword(), userDetails.getPassword())
        if (!Objects.equals(signInDto.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Password is incorrect");
        }

        return jwtProvider.generateToken(userDetails);
    }

    public UserDto signUp(SignUpDto dto) {
        if (userService.userExists(dto.getLogin())) {
            throw new IllegalArgumentException(String.format(
                    "User with login %s already exists", dto.getLogin()));
        }
        //TODO make password encoding: dto.setPassword(passwordEncoder.encodePassword(dto.getPassword()));
        var user = userMapper.fromDtoSave(dto);
        return userService.save(user);
    }

}
