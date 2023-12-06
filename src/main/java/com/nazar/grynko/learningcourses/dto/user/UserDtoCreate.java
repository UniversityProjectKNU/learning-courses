package com.nazar.grynko.learningcourses.dto.user;

import com.nazar.grynko.learningcourses.model.RoleType;
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
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDtoCreate {

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

    @NotNull
    private RoleType role;

}
