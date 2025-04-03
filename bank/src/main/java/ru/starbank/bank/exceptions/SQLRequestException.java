package ru.starbank.bank.exceptions;

public class SQLRequestException extends RuntimeException {

    public SQLRequestException(String message) {
        super(message);
    }

}
