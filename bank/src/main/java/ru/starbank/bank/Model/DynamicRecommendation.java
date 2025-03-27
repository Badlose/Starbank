package ru.starbank.bank.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class DynamicRecommendation {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private UUID product_id;

    private String text;

    private List<Rule> ruleList;

    public DynamicRecommendation(Long id, String name, UUID product_id, String text, List<Rule> ruleList) {
        this.id = id;
        this.name = name;
        this.product_id = product_id;
        this.text = text;
        this.ruleList = ruleList;
    }

    public DynamicRecommendation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getProduct_id() {
        return product_id;
    }

    public void setProduct_id(UUID product_id) {
        this.product_id = product_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DynamicRecommendation that = (DynamicRecommendation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(product_id, that.product_id) && Objects.equals(text, that.text) && Objects.equals(ruleList, that.ruleList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, product_id, text, ruleList);
    }

    @Override
    public String toString() {
        return "DynamicRecommendation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", product_id=" + product_id +
                ", text='" + text + '\'' +
                ", ruleList=" + ruleList +
                '}';
    }
}
