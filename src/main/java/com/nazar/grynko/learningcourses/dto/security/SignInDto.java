package com.nazar.grynko.learningcourses.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @Email
    @Size(max = 128)
    private String login;

    @NotNull
    @Size(min = 4, max = 64)
    private String password;

}
