package ru.starbank.bank.exceptions;

public class IncorrectProductTypeException extends RuntimeException {
    public IncorrectProductTypeException(String message) {
        super(message);
    }
}
