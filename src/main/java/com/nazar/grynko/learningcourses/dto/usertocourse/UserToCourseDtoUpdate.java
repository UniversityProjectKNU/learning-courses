package com.nazar.grynko.learningcourses.dto.usertocourse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserToCourseDtoUpdate implements Serializable {

    @Min(0)
    @Max(10_000_000)
    private Float mark;

    private String finalFeedback;

}
