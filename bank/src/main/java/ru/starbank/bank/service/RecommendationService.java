package ru.starbank.bank.service;

import org.springframework.http.HttpStatusCode;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.model.DynamicRecommendation;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {

    UserRecommendationsDTO getRecommendation(UUID userId);

    DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation);

    List<DynamicRecommendation> getAllDynamicRecommendations();

    HttpStatusCode deleteDynamicRecommendation(Long recommendationId);


}

