package ru.starbank.bank.DTO;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
