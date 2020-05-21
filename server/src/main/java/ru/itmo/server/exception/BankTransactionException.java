package ru.itmo.server.exception;

public class BankTransactionException extends ApiRequestException {

    private static final long serialVersionUID = -3128681006635769411L;

    public BankTransactionException(String message) {
        super(message);
    }
}
