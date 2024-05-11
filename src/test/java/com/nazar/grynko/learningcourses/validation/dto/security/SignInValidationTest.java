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

    @Test
    @DisplayName("Login name is more than 64. Expected (1) @Email")
        // correct login: name[1-64]@sub[0-64].domain[1-64].tld[0-64]
    void signIn_incorrectLogin3() {
        var name = new String(new char[65]).replace('\0', 'a');

        var signIn = new SignInDto()
                .setLogin(name + "@gmail.com")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login domain is less than 1. Expected (1) @Email")
    void signIn_incorrectLogin4() {
        var signIn = new SignInDto()
                .setLogin("test@")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login domain is more than 63. Expected (1) @Email")
    void signIn_incorrectLogin5() {
        var domain = new String(new char[64]).replace('\0', 'a');

        var signIn = new SignInDto()
                .setLogin("test@" + domain)
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login sub domain is less than 1. Expected (1) @Email")
    void signIn_incorrectLogin6() {
        var signIn = new SignInDto()
                .setLogin("test@.com")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login sub domain is more than 63. Expected (1) @Email")
    void signIn_incorrectLogin7() {
        var subDomain = new String(new char[64]).replace('\0', 'a');

        var signIn = new SignInDto()
                .setLogin("test@" + subDomain + ".gmail")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login tld is less than 1. Expected (1) @Email")
    void signIn_incorrectLogin8() {
        var signIn = new SignInDto()
                .setLogin("test@gmail.com.")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login tld is more than 63. Expected (1) @Email")
    void signIn_incorrectLogin9() {
        var tld = new String(new char[64]).replace('\0', 'a');

        var signIn = new SignInDto()
                .setLogin("test@gmail.com." + tld)
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login is empty. Expected (1) @NotBlank")
    void signIn_incorrectLogin10() {
        var signIn = new SignInDto()
                .setLogin("")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login is null. Expected (1) @NotBlank")
    void signIn_incorrectLogin11() {
        var signIn = new SignInDto()
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login is whitespaces. Expected (1) @NotBlank, (1) @Email")
    void signIn_incorrectLogin12() {
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
    void signIn_incorrectLogin13() {
        var signIn = new SignInDto()
                .setLogin("      @gmail.com")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login is min name and domain. Check result")
    void signIn_minimumCorrectLogin1_checkResult() {
        var signIn = new SignInDto()
                .setLogin("a@a")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Login is min name, domain, subdomain. Check result")
    void signIn_minimumCorrectLogin2_checkResult() {
        var signIn = new SignInDto()
                .setLogin("a@a.a")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Login is min name, domain, subdomain, tld. Check result")
    void signIn_minimumCorrectLogin3_checkResult() {
        var signIn = new SignInDto()
                .setLogin("a@a.a.a")
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Login is max name and domain. Check result")
    void signIn_maximumCorrectLogin1_checkResult() {
        var name = new String(new char[63]).replace('\0', 'a');
        var domain = new String(new char[63]).replace('\0', 'a');

        var signIn = new SignInDto()
                .setLogin(name + "@" + domain)
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Login is max name, domain, subdomain. Check result")
    void signIn_maximumCorrectLogin2_checkResult() {
        var name = new String(new char[63]).replace('\0', 'a');
        var domain = new String(new char[63]).replace('\0', 'a');
        var subDomain = new String(new char[63]).replace('\0', 'a');

        var signIn = new SignInDto()
                .setLogin(name + "@" + subDomain + "." + domain)
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Login is max name, domain, subdomain, tld. Check result")
    void signIn_maximumCorrectLogin3_checkResult() {
        var name = new String(new char[63]).replace('\0', 'a');
        var domain = new String(new char[63]).replace('\0', 'a');
        var subDomain = new String(new char[63]).replace('\0', 'a');
        var tld = new String(new char[63]).replace('\0', 'a');

        var signIn = new SignInDto()
                .setLogin(name + "@" + subDomain + "." + domain + "." + tld)
                .setPassword("qwerty123");

        var violations = sut.validate(signIn);

        assertThat(violations).isEmpty();
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
    @DisplayName("Password is min. Check result")
    void signIn_minimumCorrectPassword1() {
        var signIn = new SignInDto()
                .setLogin("test@gmail.com")
                .setPassword("test");

        var violations = sut.validate(signIn);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Password is max. Check result")
    void signUp_maximumCorrectPassword1() {
        var password = new String(new char[64]).replace('\0', 'a');

        var signIn = new SignInDto()
                .setLogin("test@gmail.com")
                .setPassword(password);

        var violations = sut.validate(signIn);

        assertThat(violations).isEmpty();
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
