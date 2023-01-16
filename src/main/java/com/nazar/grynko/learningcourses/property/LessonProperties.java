package com.nazar.grynko.learningcourses.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LessonProperties {

    private Integer defaultMaxMark;
    private Integer defaultSuccessMark;

}
