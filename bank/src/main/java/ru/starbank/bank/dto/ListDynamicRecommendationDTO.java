package ru.starbank.bank.dto;


import java.util.List;
import java.util.UUID;

public class ListDynamicRecommendationDTO {

    private UUID userId;
    private List<DynamicRecommendationDTO> data;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<DynamicRecommendationDTO> getData() {
        return data;
    }

    public void setData(List<DynamicRecommendationDTO> data) {
        this.data = data;
    }
}
