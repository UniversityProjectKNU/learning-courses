package com.nazar.grynko.learningcourses.dto.user;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDto {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private RoleDto role;

}
