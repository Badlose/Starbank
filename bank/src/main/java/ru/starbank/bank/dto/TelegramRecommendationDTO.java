package ru.starbank.bank.dto;

public class TelegramRecommendationDTO {

    private String productText;

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    @Override
    public String toString() {
        return productText;
    }

}
