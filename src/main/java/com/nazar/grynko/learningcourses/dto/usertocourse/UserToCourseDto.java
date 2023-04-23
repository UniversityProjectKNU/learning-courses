package com.nazar.grynko.learningcourses.dto.usertocourse;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserToCourseDto implements Serializable {

    private Long id;
    private Integer mark;
    @JsonProperty("isPassed")
    private Boolean isPassed;
    private Long userId;
    private Long courseId;

}
