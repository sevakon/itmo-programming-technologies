package ru.itmo.server.exception;

public class InvalidIdException extends BankTransactionException {

    public InvalidIdException(String message) {
        super(message);
    }

    public InvalidIdException() {
        super("Invalid Id Exception");
    }
}
