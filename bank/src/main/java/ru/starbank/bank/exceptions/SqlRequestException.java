package ru.starbank.bank.exceptions;

public class SqlRequestException extends RuntimeException {

    public SqlRequestException(String message) {
        super(message);
    }

}
