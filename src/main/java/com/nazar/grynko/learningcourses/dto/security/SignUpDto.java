package com.nazar.grynko.learningcourses.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    private String login;
    private String password;
    private String firstName;
    private String lastName;

}
