package com.nazar.grynko.learningcourses.exception.advice;

import com.nazar.grynko.learningcourses.exception.ExceptionCause;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionCause<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errorMap = new HashMap<String,String>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error-> errorMap.put(error.getField(),error.getDefaultMessage()));

        return new ExceptionCause<>(ex.getClass().getName(), ex.getMessage(), errorMap);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPathException.class)
    public ExceptionCause<?> handleInvalidPathException(InvalidPathException ex) {
        return new ExceptionCause<>(ex.getClass().getName(), ex.getMessage(), "");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ExceptionCause<?> handleAnyException(Exception ex) {
        return new ExceptionCause<>(ex.getClass().getName(), ex.getMessage(), "");
    }

}
