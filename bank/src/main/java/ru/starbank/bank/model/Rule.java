package ru.starbank.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Rule {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    private String query;

    private String arguments;

    private boolean negate;

    @ManyToOne
    @JoinColumn(name = "Recommendation_id")
    @JsonIgnore
    private DynamicRecommendation dynamicRecommendation;

    public DynamicRecommendation getDynamicRecommendation() {
        return dynamicRecommendation;
    }

    public void setDynamicRecommendation(DynamicRecommendation dynamicRecommendation) {
        this.dynamicRecommendation = dynamicRecommendation;
    }

    public Rule(String query, String arguments, boolean negate) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
    }

    public Rule(Long id, String query, String arguments, boolean negate, DynamicRecommendation dynamicRecommendation) {
        this.id = id;
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
        this.dynamicRecommendation = dynamicRecommendation;
    }

    public Rule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return negate == rule.negate && Objects.equals(id, rule.id) && Objects.equals(query, rule.query) && Objects.equals(arguments, rule.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, arguments, negate);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", query='" + query + '\'' +
                ", arguments='" + arguments + '\'' +
                ", negate=" + negate +
                '}';
    }
}
