package ru.starbank.bank.dto;

import ru.starbank.bank.model.DynamicRecommendation;

import java.util.UUID;

public class UserDTO {

    private String product_name;

    private UUID product_id;

    private String product_text;

    public UserDTO() {
    }

    public UserDTO(String product_name, UUID product_id, String product_text) {
        this.product_name = product_name;
        this.product_id = product_id;
        this.product_text = product_text;
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

//    public static UserDTO from(DynamicRecommendation recommendation) {
//        return new UserDTO(
//                recommendation.getName(),
//                recommendation.getProductId(),
//                recommendation.getText());
//    }
}

