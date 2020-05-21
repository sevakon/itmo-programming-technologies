package ru.itmo.server.exception;

public class InvalidBalanceException extends BankTransactionException {

    public InvalidBalanceException(String message) {
        super(message);
    }

    public InvalidBalanceException() {
        super("Invalid Balance Exception");
    }

}
