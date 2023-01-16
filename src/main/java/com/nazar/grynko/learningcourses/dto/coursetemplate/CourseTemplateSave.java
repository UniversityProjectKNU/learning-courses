package com.nazar.grynko.learningcourses.dto.coursetemplate;

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
public class CourseTemplateSave {
    
    private String title;
    private String description;

}
