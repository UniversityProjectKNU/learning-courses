package com.nazar.grynko.learningcourses.dto.hoeworkfile;

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
public class HomeworkFileDto {

    private Long id;
    private String title;
    private Long size;

}
