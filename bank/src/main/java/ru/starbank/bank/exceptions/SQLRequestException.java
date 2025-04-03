package ru.starbank.bank.exceptions;

public class SQLRequestException extends RuntimeException {

    public SQLRequestException() {
        System.out.println("Error executing query.");
    }

}
