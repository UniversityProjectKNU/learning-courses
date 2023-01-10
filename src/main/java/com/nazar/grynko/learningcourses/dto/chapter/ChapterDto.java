package com.nazar.grynko.learningcourses.dto.chapter;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ChapterDto implements Serializable {

    private String title;
    private String description;
    private String number;
    private boolean isFinished;
    private String finalFeedback;

}
