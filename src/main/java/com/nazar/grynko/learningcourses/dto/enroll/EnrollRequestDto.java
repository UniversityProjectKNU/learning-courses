package com.nazar.grynko.learningcourses.dto.enroll;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class EnrollRequestDto {

    private Long id;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("isApproved")
    private Boolean isApproved;
    private Long courseId;
    private Long userId;

}
