package com.nazar.grynko.learningcourses.dto.lesson;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class LessonDtoUpdate {

    @NotNull
    @Size(min = 4, max = 512)
    private String title;

    @NotNull
    @Size(min = 4, max = 2048)
    private String description;

    @NotNull
    private String number;

    @NotNull
    @JsonProperty("isFinished")
    private Boolean isFinished;

    @NotNull
    @Min(0)
    @Max(100_000)
    private Integer maxMark;

    @NotNull
    @Min(0)
    @Max(100_000)
    private Integer successMark;

}
