package com.nazar.grynko.learningcourses.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    /*@NotNull
    @Size(min = 4, max = 64)
    private String password;*/

    @NotNull
    @Size(min = 2, max = 128)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 128)
    private String lastName;

}
