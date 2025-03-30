package ru.starbank.bank.dto;

import java.util.List;
import java.util.UUID;

public class UserRecommendationsDTO {

    private UUID userId;

    private List<DynamicRecommendationDTO> recommendations;

    public UserRecommendationsDTO(UUID userId, List<DynamicRecommendationDTO> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<DynamicRecommendationDTO> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<DynamicRecommendationDTO> recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public String toString() {
        return "user_id" + ": " + userId + '\'' +
                ", recommendations" + ": " + recommendations +
                '}';
    }
}
