package ru.starbank.bank.Service;

import ru.starbank.bank.Model.Recommendation;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {

    List<Recommendation> getRecommendation(UUID userId);

}

