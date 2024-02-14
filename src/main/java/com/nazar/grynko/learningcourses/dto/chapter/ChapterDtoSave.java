package com.nazar.grynko.learningcourses.dto.chapter;

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
public class ChapterDtoSave implements Serializable {

    @NotNull
    @Size(min = 4, max = 512)
    private String title;

    @NotNull
    @Size(min = 4, max = 512)
    private String description;
    private String number;

}
