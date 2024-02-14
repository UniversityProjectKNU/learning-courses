package com.nazar.grynko.learningcourses.dto.chaptertemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ChapterTemplateDtoSave {

    @NotNull
    @Size(min = 4, max = 512)
    private String title;

    @NotNull
    @Size(min = 4, max = 2048)
    private String description;

    @NotNull
    private Integer number;

}
