package com.nazar.grynko.learningcourses.exception.advice;

import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class InvalidPathAdvice {

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(InvalidPathException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }

}
