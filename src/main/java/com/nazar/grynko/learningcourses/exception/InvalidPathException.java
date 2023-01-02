package com.nazar.grynko.learningcourses.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidPathException extends RuntimeException {

    private String message;
    private Throwable cause;

    public InvalidPathException() {
    }

    public InvalidPathException(String message) {
        this.message = message;
    }

    public InvalidPathException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

}
