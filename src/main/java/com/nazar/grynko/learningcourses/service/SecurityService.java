package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.security.SignInDto;
import com.nazar.grynko.learningcourses.dto.security.SignUpDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserDtoCreate;
import com.nazar.grynko.learningcourses.dto.user.UserSecurityDto;
import com.nazar.grynko.learningcourses.exception.BadSignInException;
import com.nazar.grynko.learningcourses.exception.BadSignUpException;
import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.mapper.UserMapper;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.security.JwtProvider;
import com.nazar.grynko.learningcourses.security.JwtUserDetailsService;
import com.nazar.grynko.learningcourses.security.MyUserDetails;
import com.nazar.grynko.learningcourses.service.internal.UserInternalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class SecurityService {

    @Value("${security.authorization.header}")
    private String AUTHORIZATION_HEADER;

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserInternalService userInternalService;
    private final UserMapper userMapper;

    private static final String USER_WITH_LOGIN_EXISTS_PATTERN = "User with login %s already exists";

    public SecurityService(JwtUserDetailsService jwtUserDetailsService,
                           JwtProvider jwtProvider,
                           PasswordEncoder passwordEncoder,
                           UserInternalService userInternalService,
                           UserMapper userMapper) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.userInternalService = userInternalService;
        this.userMapper = userMapper;
    }

    public UserSecurityDto signIn(SignInDto signInDto) {
        MyUserDetails userDetails;

        try {
            userDetails = (MyUserDetails) jwtUserDetailsService.loadUserByUsername(signInDto.getLogin());
        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            throw new BadSignInException("Login doesn't exist", e);
        }

        if (!passwordEncoder.matches(signInDto.getPassword(), userDetails.getPassword())) {
            throw new BadSignInException("Password is incorrect");
        }

        var roleValue = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .get(0);

        return new UserSecurityDto()
                .setId(userDetails.getId())
                .setLogin(signInDto.getLogin())
                .setToken(jwtProvider.generateToken(userDetails))
                .setRole(RoleType.valueOf(roleValue));
    }

    public UserDto signUp(SignUpDto dto) {
        if (userInternalService.existsUser(dto.getLogin())) {
            throw new BadSignUpException(String.format(USER_WITH_LOGIN_EXISTS_PATTERN, dto.getLogin()));
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        var user = userMapper.fromDtoSignUp(dto);
        user.setRole(RoleType.STUDENT);

        user = userInternalService.save(user);

        return userMapper.toDto(user);
    }

    public UserDto createUser(UserDtoCreate dto) {
        if (userInternalService.existsUser(dto.getLogin())) {
            throw new BadSignUpException(String.format(USER_WITH_LOGIN_EXISTS_PATTERN, dto.getLogin()));
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        var user = userMapper.fromDtoCreate(dto);

        user = userInternalService.save(user);

        return userMapper.toDto(user);
    }

    public void logout(HttpServletRequest req, HttpServletResponse resp) {
        var cookies = req.getCookies();

        if (nonNull(cookies)) {
            for (var c: cookies) {
                if (AUTHORIZATION_HEADER.equals(c.getName())) {
                    c.setValue("");
                    c.setMaxAge(0);
                    resp.addCookie(c);
                    break;
                }
            }
        }
    }
}
