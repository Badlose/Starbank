package ru.starbank.bank.Service;

import ru.starbank.bank.Model.Recommendation;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationService {
    Optional<Recommendation> getRecommendation(UUID userId);

    Integer getAmount(UUID userId);
}
