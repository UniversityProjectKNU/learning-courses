package com.nazar.grynko.learningcourses.exception.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BadSignInException extends RuntimeException {

    public BadSignInException(String message) {
        super(message);
    }

    public BadSignInException(String message, Throwable cause) {
        super(message, cause);
    }
}
