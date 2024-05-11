package com.nazar.grynko.learningcourses.exception.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class InvalidJwtTokenException extends RuntimeException {

    public InvalidJwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
