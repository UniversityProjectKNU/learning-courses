package com.nazar.grynko.learningcourses.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BadSignUpException extends RuntimeException {

    public BadSignUpException(String message) {
        super(message);
    }

}
