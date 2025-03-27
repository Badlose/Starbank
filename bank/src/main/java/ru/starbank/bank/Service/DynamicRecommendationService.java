package ru.starbank.bank.Service;

import ru.starbank.bank.Model.DynamicRecommendation;

import java.util.List;

public interface DynamicRecommendationService {

    DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation);

    List<DynamicRecommendation> getAllDynamicRecommendations();

    void deleteDynamicRecommendation(Long recommendationId);
}
