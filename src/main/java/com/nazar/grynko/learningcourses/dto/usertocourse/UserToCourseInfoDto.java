package com.nazar.grynko.learningcourses.dto.usertocourse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nazar.grynko.learningcourses.dto.course.CourseDto;
import com.nazar.grynko.learningcourses.dto.user.UserDto;
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
public class UserToCourseInfoDto implements Serializable {

    private Float mark;
    @JsonProperty("isPassed")
    private Boolean isPassed;
    private String finalFeedback;
    private UserDto user;
    private CourseDto course;

}