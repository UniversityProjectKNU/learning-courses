package com.nazar.grynko.learningcourses.validation.dto.security;

import com.nazar.grynko.learningcourses.dto.security.SignInDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignInValidationTest {

    private final Validator sut = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("All fields are null. Expected (2) @NotBlank")
    void signIn_allNull() {
        var signIn = new SignInDto();


        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(2, violations.size());

        for (var v : violations) {
            var annotation = v.getConstraintDescriptor().getAnnotation().annotationType();
            assertEquals(NotBlank.class, annotation);
        }
    }

    @Test
    @DisplayName("Login doesn't contain domain. Expected (1) @Email")
    void signIn_incorrectLogin1() {
        var signIn = new SignInDto()
                .setLogin("testgmail.com")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login doesn't contain name. Expected (1) @Email")
    void signIn_incorrectLogin2() {
        var signIn = new SignInDto()
                .setLogin("gmail.com")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

//    @Test
//    @DisplayName("Login exits maximum length of 128 characters. Expected (1) @Size")
//    void signIn_incorrectLogin3() {
//        var name = "aaaaaaa.AAAAAAA.bbbbbbbb.BBBBBBB.ccccccc.CCCCCCCC.ddddddddddddddd." +
//                "111111111111111.222222222222222.333333333333333.4444444" + "@gmail.com";
//
//        var signIn = new SignInDto()
//                .setLogin(name)
//                .setPassword("qwerty123");
//
//        var violations = sut.validate(signIn);
//
//        assertThat(violations).isNotEmpty();
//        assertEquals(1, violations.size());
//    }

    @Test
    @DisplayName("Login is empty. Expected (1) @NotBlank")
    void signIn_incorrectLogin4() {
        var signIn = new SignInDto()
                .setLogin("")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login is null. Expected (1) @NotBlank")
    void signIn_incorrectLogin5() {
        var signIn = new SignInDto()
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login is whitespaces. Expected (1) @NotBlank, (1) @Email")
    void signIn_incorrectLogin6() {
        var signIn = new SignInDto()
                .setLogin("      ")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        System.out.println(violations);
        assertThat(violations).isNotEmpty();
        assertEquals(2, violations.size());
    }

    @Test
    @DisplayName("Login is whitespaces with domain. Expected (1) @Email")
    void signIn_incorrectLogin7() {
        var signIn = new SignInDto()
                .setLogin("      @gmail.com")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Password length is less than 4. Expected (1) @Size")
    void signIn_incorrectPassword1() {
        var signIn = new SignInDto()
                .setLogin("test@gmail.com")
                .setPassword("123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Password length is more than 64. Expected (1) @Size")
    void signIn_incorrectPassword2() {
        var password = new String(new char[65]).replace('\0', 'a');

        var signIn = new SignInDto()
                .setLogin("test@gmail.com")
                .setPassword(password);

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Password is null. Expected (1) @NotBlank")
    void signIn_incorrectPassword3() {
        var signIn = new SignInDto()
                .setLogin("test@gmail.com");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Password is empty. Expected (1) @NotBlank, (1) @Size")
    void signIn_incorrectPassword4() {
        var signIn = new SignInDto()
                .setLogin("test@gmail.com")
                .setPassword("");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(2, violations.size());
    }

    @Test
    @DisplayName("Password is whitespaces. Expected (1) @NotBlank")
    void signIn_incorrectPassword5() {
        var signIn = new SignInDto()
                .setLogin("test@gmail.com")
                .setPassword("    ");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("SignInDto is correct. Check result")
    void signIn_checkResult() {
        var signIn = new SignInDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isEmpty();
    }
}
