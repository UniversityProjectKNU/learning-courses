package com.nazar.grynko.learningcourses.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> catchResourceNotFoundException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

}
