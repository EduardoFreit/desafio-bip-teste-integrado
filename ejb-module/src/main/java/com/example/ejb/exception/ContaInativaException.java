package com.example.ejb.exception;

public class ContaInativaException extends Exception {

    public ContaInativaException() {
        super("Conta inativa");
    }

    public ContaInativaException(String msg) {
        super(msg);
    }
}
