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

    private Long id;
    private String title;
    private String description;
    private Integer number;
    @JsonProperty("isFinished")
    private Boolean isFinished;
    private Integer maxMark;
    private Integer successMark;
    private Long chapterId;
    private Long courseId;

}
