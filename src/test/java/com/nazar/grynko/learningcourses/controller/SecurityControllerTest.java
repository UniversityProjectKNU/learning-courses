package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.security.JwtAuthenticationFilter;
import com.nazar.grynko.learningcourses.service.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.nazar.grynko.learningcourses.util.MockEntities.*;
import static com.nazar.grynko.learningcourses.util.MockUtil.toJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SecurityControllerTest {

    private final static String BASE_URL = "/learning-courses/api/v1";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private JwtAuthenticationFilter filter;

    @Test
    void signIn_200_checkResult() throws Exception {
        when(securityService.signIn(any())).thenReturn(mockUserSecurityDto());
        this.mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(mockSignInDto())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void signIn_400_checkResult() throws Exception {
        when(securityService.signIn(any())).thenThrow(new RuntimeException());
        this.mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(mockSignInDto())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("signIn_400: incorrect email. It doesn't contain @ sign")
    void signIn_400_incorrectEmail() throws Exception {
        var signInDto = mockSignInDto()
                .setLogin("test.gmail.com");

        this.mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(signInDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("signIn_400: incorrect email. It doesn't contain name before @ sign")
    void signIn_400_incorrectEmail2() throws Exception {
        var signInDto = mockSignInDto()
                .setLogin("@gmail.com");

        this.mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(signInDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("signIn_400: incorrect password. Length < 4")
    void signIn_400_incorrectPassword() throws Exception {
        var signInDto = mockSignInDto()
                .setPassword("pas");

        this.mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(signInDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("signIn_400: incorrect password. Length > 64")
    void signIn_400_incorrectPassword2() throws Exception {
        var signInDto = mockSignInDto()
                .setPassword("paaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaassword");

        this.mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(signInDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_200_checkResult() throws Exception {
        when(securityService.signUp(any())).thenReturn(mockUserDto());
        this.mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(mockSignUpDto())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void signUp_400_checkResult() throws Exception {
        when(securityService.signUp(any())).thenThrow(new RuntimeException());
        this.mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(mockSignUpDto())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("signUp_400: incorrect email. It doesn't contain @ sign")
    void signUp_400_incorrectEmail() throws Exception {
        var signInDto = mockSignUpDto()
                .setLogin("test.gmail.com");

        this.mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(signInDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("signUp_400: incorrect email. It doesn't contain name before @ sign")
    void signUp_400_incorrectEmail2() throws Exception {
        var signInDto = mockSignUpDto()
                .setLogin("@gmail.com");

        this.mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(signInDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("signIn_400: incorrect password. Length < 4")
    void signUp_400_incorrectPassword() throws Exception {
        var signInDto = mockSignUpDto()
                .setPassword("pas");

        this.mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(signInDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("signIn_400: incorrect password. Length > 64")
    void signUp_400_incorrectPassword2() throws Exception {
        var signInDto = mockSignUpDto()
                .setPassword("paaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaassword");

        this.mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(signInDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
