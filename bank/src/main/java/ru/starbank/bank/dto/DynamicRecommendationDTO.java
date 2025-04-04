package ru.starbank.bank.dto;

import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;

import java.util.List;
import java.util.UUID;

public class DynamicRecommendationDTO {

    private Long id;

    private String product_name;

    private UUID product_id;

    private String product_text;

    private List<Rule> rule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public UUID getProduct_id() {
        return product_id;
    }

    public void setProduct_id(UUID product_id) {
        this.product_id = product_id;
    }

    public String getProduct_text() {
        return product_text;
    }

    public void setProduct_text(String product_text) {
        this.product_text = product_text;
    }

    public List<Rule> getRule() {
        return rule;
    }

    public void setRule(List<Rule> rule) {
        this.rule = rule;
    }

//    public static DynamicRecommendationDTO from(DynamicRecommendation recommendation, List<RuleDTO> ruleDTOList) {
//        return new DynamicRecommendationDTO(
//                recommendation.getId(),
//                recommendation.getName(),
//                recommendation.getProductId(),
//                recommendation.getText(),
//                ruleDTOList);
//    }

}
