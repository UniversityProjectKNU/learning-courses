package com.nazar.grynko.learningcourses.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LessonDtoSave implements Serializable {

    @NotNull
    @Size(min = 4, max = 512)
    private String title;
    @NotNull
    @Size(min = 4, max = 2048)
    private String description;
    private String number;
    private Integer maxMark;
    private Integer successMark;

}
