package ru.starbank.bank.dto;

import ru.starbank.bank.model.Rule;

import java.util.List;

public class RuleDTO {
    private String query;
    private List<String> arguments;
    private boolean negate;

    public RuleDTO() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

//    public static RuleDTO from(Rule rule) {
//        return new RuleDTO(
//                rule.getQuery(),
//                rule.getArguments(),
//                rule.isNegate());
//    }
}
