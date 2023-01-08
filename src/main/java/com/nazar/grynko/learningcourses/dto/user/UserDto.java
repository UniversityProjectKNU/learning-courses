package com.nazar.grynko.learningcourses.dto.user;

import com.nazar.grynko.learningcourses.dto.role.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Calendar;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDto {

    private String firstName;
    private String lastName;
    private Calendar dateOfBirth;
    private Set<RoleDto> roles;

}
