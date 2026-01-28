package com.example.backend.exception;

import org.springframework.http.HttpStatus;

import com.example.backend.enuns.BackEndExceptionEnum;

/**
 *
 * Exceção personalizada para validações no backend
 * @author lepf9
 */
public class ValidacaoException extends RuntimeException {

    public ValidacaoException(String message) {
        super(message);
    }

}
