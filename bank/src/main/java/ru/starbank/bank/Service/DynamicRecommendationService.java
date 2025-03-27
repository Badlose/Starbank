package ru.starbank.bank.Service;

import org.springframework.http.HttpStatus;
import ru.starbank.bank.Model.DynamicRecommendation;

import java.util.List;

public interface DynamicRecommendationService {

    DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation);

    List<DynamicRecommendation> getAllDynamicRecommendations();

    HttpStatus deleteDynamicRecommendation(Long recommendationId);
}
