package com.nazar.grynko.learningcourses.dto.lesson;

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
public class LessonDtoSave implements Serializable {

    private String title;
    private String description;
    private String number;
    private Integer maxMark;
    private Integer successMark;

}
