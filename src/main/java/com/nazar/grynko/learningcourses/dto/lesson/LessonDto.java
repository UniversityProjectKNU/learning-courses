package com.nazar.grynko.learningcourses.dto.lesson;

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
public class LessonDto implements Serializable {

    private String title;
    private String description;
    private String number;
    @JsonProperty("isFinished")
    private boolean isFinished;
    private Integer maxMark;
    private Integer successMark;

}
