package ru.starbank.bank.DTO;


import java.util.List;

public class ListDynamicRecommendationDTO {

    private List<DynamicRecommendationDTO> data;

    public ListDynamicRecommendationDTO(List<DynamicRecommendationDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}
