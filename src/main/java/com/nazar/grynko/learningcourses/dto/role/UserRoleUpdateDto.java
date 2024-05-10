package com.nazar.grynko.learningcourses.dto.role;

import com.nazar.grynko.learningcourses.model.RoleType;
import jakarta.validation.constraints.NotNull;
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
public class UserRoleUpdateDto {

    @NotNull
    private RoleType type;

}
