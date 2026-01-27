package com.example.ejb.exception;

/**
 * Exceção lançada quando uma conta não é encontrada.
 * @author lepf9
 */
public class ContaNaoEncontradaException extends Exception {
    public ContaNaoEncontradaException(String message) {
        super(message);
    }

    public ContaNaoEncontradaException() {
        super("Conta não encontrada");
    }
}
