package ru.starbank.bank.DTO;

import ru.starbank.bank.Model.Rule;

import java.util.List;
import java.util.UUID;
public class DynamicRecommendationDTO {

    private Long id;

    private String product_name;

    private UUID product_id;

    private String product_text;

    private List<Rule> rule;

    public DynamicRecommendationDTO(Long id, String product_name, UUID product_id, String product_text, List<Rule> rule) {
        this.id = id;
        this.product_name = product_name;
        this.product_id = product_id;
        this.product_text = product_text;
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", product_name='" + product_name + '\'' +
                ", product_id=" + product_id +
                ", product_text='" + product_text + '\'' +
                ", rule=" + rule +
                '}';
    }
}
