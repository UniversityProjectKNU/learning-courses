package com.nazar.grynko.learningcourses.dto.lessontemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LessonTemplateDtoUpdate {

    private Long id;
    private String title;
    private String description;
    private Integer number;
    private Integer maxMark;
    private Integer successMark;

}
