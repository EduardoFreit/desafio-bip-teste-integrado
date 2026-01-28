package com.example.ejb.exception;

/**
 * Exceção lançada quando uma conta não é encontrada.
 * @author lepf9
 */
public class ContaNaoEncontradaException extends RuntimeException {
    public ContaNaoEncontradaException(String message) {
        super(message);
    }

    public ContaNaoEncontradaException() {
        super("Conta não encontrada");
    }
}
