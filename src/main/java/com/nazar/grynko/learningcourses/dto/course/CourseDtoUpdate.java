package com.nazar.grynko.learningcourses.dto.course;

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
public class CourseDtoUpdate implements Serializable {

    private Long id;
    private String title;
    private String description;
    @JsonProperty("isFinished")
    private Boolean isFinished;
    private String finalFeedback;

}
