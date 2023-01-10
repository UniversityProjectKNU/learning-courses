package com.nazar.grynko.learningcourses.dto.course;

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
public class CourseDto implements Serializable {

    private String title;
    private String description;
    private boolean isFinished;
    private String finalFeedback;

}
