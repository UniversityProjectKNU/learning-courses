package com.nazar.grynko.learningcourses.dto.user;

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
public class UserDtoUpdate {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;

}
