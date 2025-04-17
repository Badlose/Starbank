package ru.starbank.bank.dto;


import java.util.List;

public class ListDynamicRecommendationDTO {

    private List<DynamicRecommendationDTO> data;

    public ListDynamicRecommendationDTO() {
    }

    public ListDynamicRecommendationDTO(List<DynamicRecommendationDTO> data) {
        this.data = data;
    }

    public List<DynamicRecommendationDTO> getData() {
        return data;
    }

    public void setData(List<DynamicRecommendationDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ListDynamicRecommendationDTO{" +
                "data=" + data +
                '}';
    }
}
