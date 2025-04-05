package ru.starbank.bank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IncorrectRuleException extends RuntimeException {

    public IncorrectRuleException() {
        System.out.println("Rule or rule arguments are null or empty.");
    }

}
