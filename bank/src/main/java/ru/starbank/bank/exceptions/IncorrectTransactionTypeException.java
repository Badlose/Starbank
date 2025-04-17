package ru.starbank.bank.exceptions;

public class IncorrectTransactionTypeException extends RuntimeException {

    public IncorrectTransactionTypeException(String message) {
        super(message);
    }
}
