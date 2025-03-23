package ru.starbank.bank.Service;

import ru.starbank.bank.Model.Recommendation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationService {
    Optional<List<Recommendation>> getRecommendation(UUID userId);

    Integer getAmount(UUID userId);

    List<Recommendation> getRecommendationsWithLogging(UUID userId);
}

