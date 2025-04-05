package ru.starbank.bank.exceptions;

public class IncorrectRuleQueryException extends RuntimeException {

    public IncorrectRuleQueryException(String message) {
        super(message);
    }
}
