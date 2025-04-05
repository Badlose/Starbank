package ru.starbank.bank.exceptions;

public class IllegalResultException extends RuntimeException {

    public IllegalResultException() {
        System.out.println("Query returned null");
    }
}
