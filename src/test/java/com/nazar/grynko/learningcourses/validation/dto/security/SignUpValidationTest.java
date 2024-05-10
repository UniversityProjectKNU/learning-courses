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

//    @Test
//    @DisplayName("Login exits maximum length of 128 characters. Expected (1) @Size")
//    void signUp_incorrectLogin3() {
//        var name = "aaaaaaa.AAAAAAA.bbbbbbbb.BBBBBBB.ccccccc.CCCCCCCC.ddddddddddddddd." +
//                "111111111111111.222222222222222.333333333333333.4444444" + "@gmail.com";
//
//        var signUp = new SignUpDto()
//                .setLogin(name)
//                .setPassword("qwerty123");
//
//        var violations = sut.validate(signUp);
//
//        assertThat(violations).isNotEmpty();
//        assertEquals(1, violations.size());
//    }

    @Test
    @DisplayName("Login is empty. Expected (1) @NotBlank")
    void signUp_incorrectLogin4() {
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
    void signUp_incorrectLogin5() {
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
    void signUp_incorrectLogin6() {
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
    void signUp_incorrectLogin7() {
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
