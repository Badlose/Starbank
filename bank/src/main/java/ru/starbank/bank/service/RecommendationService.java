package ru.starbank.bank.service;

import org.springframework.http.HttpStatusCode;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.dto.ListDynamicRecommendationDTO;
import ru.starbank.bank.dto.StatisticsDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.model.DynamicRecommendation;

import java.util.UUID;

public interface RecommendationService {

    UserRecommendationsDTO getRecommendation(UUID userId);

    DynamicRecommendationDTO createNewDynamicRecommendation(DynamicRecommendation recommendation);

    ListDynamicRecommendationDTO getAllDynamicRecommendations();

    void deleteDynamicRecommendation(Long recommendationId);


    StatisticsDTO getStatistics();
}

