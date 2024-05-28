package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.security.SignInDto;
import com.nazar.grynko.learningcourses.dto.security.SignUpDto;
import com.nazar.grynko.learningcourses.exception.domain.BadSignInException;
import com.nazar.grynko.learningcourses.exception.domain.BadSignUpException;
import com.nazar.grynko.learningcourses.security.JwtAuthenticationFilter;
import com.nazar.grynko.learningcourses.service.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.nazar.grynko.learningcourses.util.MockEntities.mockUserDto;
import static com.nazar.grynko.learningcourses.util.MockEntities.mockUserSecurityDto;
import static com.nazar.grynko.learningcourses.util.RequestJson.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SecurityControllerTest {

    private final static String BASE_URL = "/learning-courses/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private JwtAuthenticationFilter filter;

    @Test
    void signIn_200_checkResult() throws Exception {
        when(securityService.signIn(any(SignInDto.class)))
                .thenReturn(mockUserSecurityDto());

        this.mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(SIGN_IN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.login").value("test.user@gmail.com"))
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @DisplayName("signIn_401: login or password is incorrect")
    void signIn_401() throws Exception {
        when(securityService.signIn(any(SignInDto.class)))
                .thenThrow(new BadSignInException());

        this.mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(SIGN_IN))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("signIn_400: incorrect body")
    void signIn_400() throws Exception {
        when(securityService.signIn(any(SignInDto.class)))
                .thenThrow(new BadSignInException());

        this.mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(EMPTY_BODY))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_200_checkResult() throws Exception {
        when(securityService.signUp(any(SignUpDto.class))).thenReturn(mockUserDto());

        this.mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(SIGN_UP))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.login").value("test.user@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Nazar"))
                .andExpect(jsonPath("$.lastName").value("Grynko"));
    }

    @Test
    @DisplayName("signUp_401: password is incorrect or such user already exists")
    void signUp_401() throws Exception {
        when(securityService.signUp(any(SignUpDto.class)))
                .thenThrow(new BadSignUpException());

        this.mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(SIGN_UP))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("signUp_400: incorrect body")
    void signUp_400() throws Exception {
        when(securityService.signIn(any(SignInDto.class)))
                .thenThrow(new BadSignInException());

        this.mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(EMPTY_BODY))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
