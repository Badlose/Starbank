package ru.starbank.bank.Service;

import org.springframework.http.HttpStatusCode;
import ru.starbank.bank.Model.DynamicRecommendation;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {

    List<DynamicRecommendation> getRecommendation(UUID userId);

    DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation);

    List<DynamicRecommendation> getAllDynamicRecommendations();

    HttpStatusCode deleteDynamicRecommendation(Long recommendationId);


}

