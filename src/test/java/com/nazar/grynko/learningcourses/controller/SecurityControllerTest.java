package com.nazar.grynko.learningcourses.controller;

import com.nazar.grynko.learningcourses.dto.security.SignInDto;
import com.nazar.grynko.learningcourses.dto.security.SignUpDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
import com.nazar.grynko.learningcourses.dto.user.UserSecurityDto;
import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.security.JwtAuthenticationFilter;
import com.nazar.grynko.learningcourses.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.nazar.grynko.learningcourses.util.MockUtil.toJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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

    private SignInDto mockSignInDto() {
        return new SignInDto()
                .setLogin("test.user@gmail.com")
                .setPassword("test.password");
    }

    private SignUpDto mockSignUpDto() {
        return new SignUpDto()
                .setLogin("test.user@gmail.com")
                .setPassword("test.password")
                .setFirstName("testFirstName")
                .setLastName("testLastName");
    }

    private UserSecurityDto mockUserSecurityDto() {
        return new UserSecurityDto()
                .setId(100L)
                .setRole(RoleType.STUDENT)
                .setLogin("test.user@gmail.com")
                .setToken("token");
    }

    private UserDto mockUserDto() {
        return new UserDto()
                .setId(100L)
                .setRole(RoleType.STUDENT)
                .setLogin("test.user@gmail.com")
                .setFirstName("testFirstName")
                .setLastName("testLastName");
    }

    @Test
    void signIn_200_checkResult() throws Exception {
        when(securityService.signIn(any())).thenReturn(mockUserSecurityDto());
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(mockSignInDto())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void signIn_400_checkResult() throws Exception {
        when(securityService.signIn(any())).thenThrow(new RuntimeException());
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(mockSignInDto())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_200_checkResult() throws Exception {
        when(securityService.signUp(any())).thenReturn(mockUserDto());
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(mockSignUpDto())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void signUp_400_checkResult() throws Exception {
        when(securityService.signUp(any())).thenThrow(new RuntimeException());
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(BASE_URL + "/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(toJsonString(mockSignUpDto())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
