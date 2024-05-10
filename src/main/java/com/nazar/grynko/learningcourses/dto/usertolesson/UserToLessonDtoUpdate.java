package com.nazar.grynko.learningcourses.dto.usertolesson;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserToLessonDtoUpdate implements Serializable {

    @Min(0)
    @Max(100_000)
    private Integer mark;

}
