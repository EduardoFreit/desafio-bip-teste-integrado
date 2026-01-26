package com.example.backend.dto;

import com.example.backend.enuns.BackEndExceptionEnum;

/**
 *
 * DTO para resposta de falhas no backend
 * @author lepf9
 */
public record FalhaBackendResponse(String message, BackEndExceptionEnum codigo) {

}
