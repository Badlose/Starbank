package ru.starbank.bank.dto;

import java.util.List;
import java.util.UUID;

public class UserRecommendationsDTO {

    private UUID userId;

    private List<UserDTO> recommendations;

    public UserRecommendationsDTO(UUID userId, List<UserDTO> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public UserRecommendationsDTO() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<UserDTO> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<UserDTO> recommendations) {
        this.recommendations = recommendations;
    }

}
