package com.example.ejb.exception;

/**
 * Exceção lançada quando o saldo é insuficiente.
 * @author lepf9
 */
public class SaldoInsuficienteException extends Exception {
    public SaldoInsuficienteException(String message) {
        super(message);
    }

    public SaldoInsuficienteException() {
        super("Saldo insuficiente");
    }
}
