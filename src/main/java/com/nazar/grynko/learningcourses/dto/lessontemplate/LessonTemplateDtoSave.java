package com.nazar.grynko.learningcourses.dto.lessontemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LessonTemplateDtoSave {

    @NotNull
    @Size(min = 4, max = 512)
    private String title;

    @NotNull
    @Size(min = 4, max = 2048)
    private String description;

    @NotNull
    private Integer number;

    @NotNull
    @Min(0)
    @Max(100_000)
    private Integer maxMark;

    @NotNull
    @Min(0)
    @Max(100_000)
    private Integer successMark;

}
