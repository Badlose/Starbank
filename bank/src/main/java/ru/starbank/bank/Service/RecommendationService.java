package ru.starbank.bank.Service;

import ru.starbank.bank.Model.DynamicRecommendation;
import ru.starbank.bank.Model.Recommendation;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {

    List<Recommendation> getRecommendation(UUID userId);

    DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation);

    List<DynamicRecommendation> getAllDynamicRecommendations();

    void deleteDynamicRecommendation(Long recommendationId);

}

