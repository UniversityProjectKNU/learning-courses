package com.nazar.grynko.learningcourses.dto.chaptertemplate;

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
public class ChapterTemplateDto {

    private Long id;
    private String title;
    private String description;
    private Integer number;
    private Long courseTemplateId;

}
