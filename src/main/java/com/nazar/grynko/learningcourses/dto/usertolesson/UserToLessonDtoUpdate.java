package com.nazar.grynko.learningcourses.dto.usertolesson;

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
public class UserToLessonDtoUpdate implements Serializable {

    private Integer mark;
    @JsonProperty("isPassed")
    private Boolean isPassed;

}
