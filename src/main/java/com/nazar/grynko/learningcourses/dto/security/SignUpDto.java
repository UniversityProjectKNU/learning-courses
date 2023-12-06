package com.nazar.grynko.learningcourses.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SignUpDto {

    @NotNull
    @Email
    @Size(max = 128)
    private String login;

    @NotNull
    @Size(min = 4, max = 64)
    private String password;

    @NotNull
    @Size(min = 2, max = 128)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 128)
    private String lastName;

}
