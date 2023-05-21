package com.nazar.grynko.learningcourses.dto.user;

import com.nazar.grynko.learningcourses.model.RoleType;
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
public class UserSecurityDto {

    private String login;
    private RoleType role;
    private String token;

}
