package com.nazar.grynko.learningcourses.exception.advice;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidPathAdvice {

    @ResponseBody
    @ExceptionHandler(InvalidPathException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(InvalidPathException ex) {
        return ex.getMessage();
    }

}
