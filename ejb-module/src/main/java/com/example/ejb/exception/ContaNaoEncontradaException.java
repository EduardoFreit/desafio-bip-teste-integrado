package com.example.ejb.exception;


public class ContaNaoEncontradaException extends Exception {
    public ContaNaoEncontradaException(String message) {
        super(message);
    }

    public ContaNaoEncontradaException() {
        super("Conta não encontrada");
    }
}
