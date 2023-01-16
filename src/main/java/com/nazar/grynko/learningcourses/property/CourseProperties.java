package com.nazar.grynko.learningcourses.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CourseProperties {

    @Value("${course.default.is.finished}")
    private Boolean defaultIsFinished;

}
