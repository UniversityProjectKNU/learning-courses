package com.nazar.grynko.learningcourses.validation.dto.security;

import com.nazar.grynko.learningcourses.dto.security.SignUpDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignUpValidationTest {

    private final Validator sut = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("All fields are null. Expected (4) @NotBlank")
    void signUp_allNull() {
        var signUp = new SignUpDto();

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(4, violations.size());

        for (var v : violations) {
            var annotation = v.getConstraintDescriptor().getAnnotation().annotationType();
            assertEquals(NotBlank.class, annotation);
        }
    }

    @Test
    @DisplayName("Login doesn't contain domain. Expected (1) @Email")
    void signUp_incorrectLogin1() {
        var signUp = new SignUpDto()
                .setLogin("testgmail.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login doesn't contain name. Expected (1) @Email")
    void signUp_incorrectLogin2() {
        var signUp = new SignUpDto()
                .setLogin("gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login name is more than 64. Expected (1) @Email")
        // correct login: name[1-64]@sub[0-64].domain[1-64].tld[0-64]
    void signUp_incorrectLogin3() {
        var name = new String(new char[65]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin(name + "@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login domain is less than 1. Expected (1) @Email")
    void signUp_incorrectLogin4() {
        var signUp = new SignUpDto()
                .setLogin("test@")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login domain is more than 63. Expected (1) @Email")
    void signUp_incorrectLogin5() {
        var domain = new String(new char[64]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin("test@" + domain)
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login sub domain is less than 1. Expected (1) @Email")
    void signUp_incorrectLogin6() {
        var signUp = new SignUpDto()
                .setLogin("test@.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login sub domain is more than 63. Expected (1) @Email")
    void signUp_incorrectLogin7() {
        var subDomain = new String(new char[64]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin("test@" + subDomain + ".gmail")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login tld is less than 1. Expected (1) @Email")
    void signUp_incorrectLogin8() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com.")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login tld is more than 63. Expected (1) @Email")
    void signUp_incorrectLogin9() {
        var tld = new String(new char[64]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin("test@gmail.com." + tld)
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login is empty. Expected (1) @NotBlank")
    void signUp_incorrectLogin10() {
        var signUp = new SignUpDto()
                .setLogin("")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login is null. Expected (1) @NotBlank")
    void signUp_incorrectLogin11() {
        var signUp = new SignUpDto()
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login is whitespaces. Expected (1) @NotBlank, (1) @Email")
    void signUp_incorrectLogin12() {
        var signUp = new SignUpDto()
                .setLogin("      ")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        System.out.println(violations);
        assertThat(violations).isNotEmpty();
        assertEquals(2, violations.size());
    }

    @Test
    @DisplayName("Login is whitespaces with domain. Expected (1) @Email")
    void signUp_incorrectLogin13() {
        var signUp = new SignUpDto()
                .setLogin("      @gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Login is min name and domain. Check result")
    void signUp_minimumCorrectLogin1_checkResult() {
        var signUp = new SignUpDto()
                .setLogin("a@a")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Login is min name, domain, subdomain. Check result")
    void signUp_minimumCorrectLogin2_checkResult() {
        var signUp = new SignUpDto()
                .setLogin("a@a.a")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Login is min name, domain, subdomain, tld. Check result")
    void signUp_minimumCorrectLogin3_checkResult() {
        var signUp = new SignUpDto()
                .setLogin("a@a.a.a")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Login is max name and domain. Check result")
    void signUp_maximumCorrectLogin1_checkResult() {
        var name = new String(new char[63]).replace('\0', 'a');
        var domain = new String(new char[63]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin(name + "@" + domain)
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Login is max name, domain, subdomain. Check result")
    void signUp_maximumCorrectLogin2_checkResult() {
        var name = new String(new char[63]).replace('\0', 'a');
        var domain = new String(new char[63]).replace('\0', 'a');
        var subDomain = new String(new char[63]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin(name + "@" + subDomain + "." + domain)
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Login is max name, domain, subdomain, tld. Check result")
    void signUp_maximumCorrectLogin3_checkResult() {
        var name = new String(new char[63]).replace('\0', 'a');
        var domain = new String(new char[63]).replace('\0', 'a');
        var subDomain = new String(new char[63]).replace('\0', 'a');
        var tld = new String(new char[63]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin(name + "@" + subDomain + "." + domain + "." + tld)
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Password length is less than 4. Expected (1) @Size")
    void signUp_incorrectPassword1() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Password length is less than 64. Expected (1) @Size")
    void signUp_incorrectPassword2() {
        var password = new String(new char[65]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword(password)
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Password is null. Expected (1) @NotBlank")
    void signUp_incorrectPassword3() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Password is empty. Expected (1) @NotBlank, (1) @Size")
    void signUp_incorrectPassword4() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(2, violations.size());
    }

    @Test
    @DisplayName("Password is whitespaces. Expected (1) @NotBlank")
    void signUp_incorrectPassword5() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("    ")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Password is min. Check result")
    void signIn_minimumCorrectPassword1() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("test")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Password is max. Check result")
    void signUp_maximumCorrectPassword1() {
        var password = new String(new char[64]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword(password)
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("First name length is less than 4. Expected (1) @Size")
    void signUp_incorrectFirstName1() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("N")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("First name length is more than 128. Expected (1) @Size")
    void signUp_incorrectFirstName2() {
        var firstName = new String(new char[129]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName(firstName)
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("First name is null. Expected (1) @NotBlank")
    void signUp_incorrectFirstName3() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("First name is empty. Expected (1) @NotBlank, (1) @Size")
    void signUp_incorrectFirstName4() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(2, violations.size());
    }

    @Test
    @DisplayName("First name is empty. Expected (1) @NotBlank")
    void signUp_incorrectFirstName5() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("    ")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("First name is min. Check result")
    void signUp_minimumCorrectFirstName1() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Ye")
                .setLastName("West");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("First name is max. Check result")
    void signUp_maximumCorrectFirstName1() {
        var firstName = new String(new char[128]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName(firstName)
                .setLastName("West");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Last name length is less than 4. Expected (1) @Size")
    void signUp_incorrectLastName1() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("G");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Last name length is more than 128. Expected (1) @Size")
    void signUp_incorrectLastName2() {
        var lastName = new String(new char[129]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName(lastName);

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Last name is null. Expected (1) @NotBlank")
    void signUp_incorrectLastName3() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Last name is empty. Expected (1) @NotBlank, (1) @Size")
    void signUp_incorrectLastName4() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(2, violations.size());
    }

    @Test
    @DisplayName("Last name is empty. Expected (1) @NotBlank")
    void signUp_incorrectLastName5() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("    ");

        var violations = sut.validate(signUp);

        assertThat(violations).isNotEmpty();
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Last name is min. Check result")
    void signUp_minimumCorrectLastName1() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Maxim")
                .setLastName("Ak");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Last name is max. Check result")
    void signUp_maximumCorrectLastName1() {
        var lastName = new String(new char[128]).replace('\0', 'a');

        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Maxim")
                .setLastName(lastName);

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("SignUpDto is correct. Check result")
    void signUp_checkResult() {
        var signUp = new SignUpDto()
                .setLogin("test@gmail.com")
                .setPassword("qwerty123")
                .setFirstName("Nazar")
                .setLastName("Grynko");

        var violations = sut.validate(signUp);

        assertThat(violations).isEmpty();
    }
}
