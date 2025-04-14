package ru.starbank.bank.exceptions;

public class EmptyRuleArgumentsException extends RuntimeException {

    public EmptyRuleArgumentsException(String message) {
        super(message);
    }
}
