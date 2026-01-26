package com.example.backend.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.backend.exception.BackendException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BackendException.class)
    public ResponseEntity<String> handleBackendException(BackendException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

}
