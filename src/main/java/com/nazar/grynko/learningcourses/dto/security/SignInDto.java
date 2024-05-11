package com.nazar.grynko.learningcourses.dto.security;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SignInDto {

    @NotBlank
    @Email
    private String login;

    @NotBlank
    @Size(min = 4, max = 64)
    private String password;

}
