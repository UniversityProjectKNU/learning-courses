package com.nazar.grynko.learningcourses.exception.advice;

import com.nazar.grynko.learningcourses.exception.BadSignInException;
import com.nazar.grynko.learningcourses.exception.BadSignUpException;
import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.exception.ExceptionCause;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler { //todo logging

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionCause<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errorMap = new HashMap<String, String>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));

        return new ExceptionCause<>(ex.getClass().getName(), ex.getMessage(), errorMap);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ExceptionCause<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ExceptionCause<>(ex.getClass().getName(), ex.getMessage(), "");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadSignInException.class)
    public ExceptionCause<?> handleBadSignInException(BadSignInException ex) {
        return new ExceptionCause<>(ex.getClass().getName(), ex.getMessage(), "");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadSignUpException.class)
    public ExceptionCause<?> handleBadSignUpException(BadSignUpException ex) {
        return new ExceptionCause<>(ex.getClass().getName(), ex.getMessage(), "");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ExceptionCause<?> handleAnyException(Exception ex) {
        return new ExceptionCause<>(ex.getClass().getName(), "Unknown exception happened", "");
    }

}
