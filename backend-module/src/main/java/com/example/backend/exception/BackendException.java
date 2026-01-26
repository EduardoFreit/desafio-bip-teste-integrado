package com.example.backend.exception;

import org.springframework.http.HttpStatus;

import com.example.backend.enuns.BackEndExceptionEnum;

/**
 *
 * Exceção personalizada para o backend
 * @author lepf9
 */
public class BackendException extends RuntimeException {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private BackEndExceptionEnum exceptionEnum = BackEndExceptionEnum.ERRO_GENERICO;

    public BackendException(String message) {
        super(message);
    }

    public BackendException(String message, BackEndExceptionEnum exceptionEnum) {
        super(message);
        this.exceptionEnum = exceptionEnum;
    }

    public BackendException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BackendException(String message, HttpStatus status, BackEndExceptionEnum exceptionEnum) {
        super(message);
        this.status = status;
        this.exceptionEnum = exceptionEnum;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public BackEndExceptionEnum getExEnum() {
        return exceptionEnum;
    }
}
