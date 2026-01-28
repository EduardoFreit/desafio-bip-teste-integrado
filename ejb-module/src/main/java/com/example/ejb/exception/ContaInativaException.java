package com.example.ejb.exception;

/**
 * Exceção lançada quando uma conta está inativa.
 * @author lepf9
 */
public class ContaInativaException extends RuntimeException {

    public ContaInativaException() {
        super("Conta inativa");
    }

    public ContaInativaException(String msg) {
        super(msg);
    }
}
