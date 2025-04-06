package ru.starbank.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class DynamicRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String name;

    private UUID productId;

    private String text;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dynamicRecommendation")
    private List<Rule> ruleList;

    public DynamicRecommendation(String name, UUID productId, String text, List<Rule> ruleList) {
        this.name = name;
        this.productId = productId;
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

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicRecommendation that = (DynamicRecommendation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(productId, that.productId) && Objects.equals(text, that.text) && Objects.equals(ruleList, that.ruleList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, productId, text, ruleList);
    }

    @Override
    public String toString() {
        return "DynamicRecommendation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", product_id=" + productId +
                ", text='" + text + '\'' +
                ", ruleList=" + ruleList +
                '}';
    }

}
