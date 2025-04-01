package ru.starbank.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String query;

    private List<String> arguments;

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

    public Rule(String query, List<String> arguments, boolean negate) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
    }

    public Rule(Long id, String query, List<String> arguments, boolean negate, DynamicRecommendation dynamicRecommendation) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return negate == rule.negate && Objects.equals(id, rule.id) && Objects.equals(query, rule.query) && Objects.equals(arguments, rule.arguments) && Objects.equals(dynamicRecommendation, rule.dynamicRecommendation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, arguments, negate, dynamicRecommendation);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", query='" + query + '\'' +
                ", arguments=" + arguments +
                ", negate=" + negate +
                ", dynamicRecommendation=" + dynamicRecommendation +
                '}';
    }
}
