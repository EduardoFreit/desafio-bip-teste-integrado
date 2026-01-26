package com.example.backend.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.backend.dto.FalhaBackendResponse;
import com.example.backend.exception.BackendException;

/**
 *
 * Handler global para exceções do backend
 * @author lepf9
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BackendException.class)
    public ResponseEntity<FalhaBackendResponse> handleBackendException(BackendException ex) {
        return new ResponseEntity<FalhaBackendResponse>(new FalhaBackendResponse(ex.getMessage(), ex.getExEnum()), ex.getStatus());
    }

}
