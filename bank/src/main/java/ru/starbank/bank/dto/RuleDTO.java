package ru.starbank.bank.dto;

import java.util.List;

public class RuleDTO {
    private String query;
    private List<String> arguments;
    private boolean negate;

    public RuleDTO(String query, List<String> arguments, boolean negate) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
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

    @Override
    public String toString() {
        return "query" + ": " + query + '\'' +
                ", arguments" + ": " + arguments +
                ", negate" + ": " + negate +
                '}';
    }
}
