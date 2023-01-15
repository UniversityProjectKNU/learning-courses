package com.nazar.grynko.learningcourses.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "lesson")
public class LessonProperties {

    private Integer defaultMaxMark;
    private Integer defaultSuccessMark;

}
