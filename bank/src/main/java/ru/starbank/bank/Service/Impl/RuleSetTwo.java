package ru.starbank.bank.Service.Impl;

import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.RecommendationService;

import java.util.Optional;
import java.util.UUID;

@Component
public class RuleSetTwo implements RecommendationService {

    private final RecommendationsRepository repository;

    public RuleSetTwo(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Recommendation> getRecommendation(UUID userId) {

        return Optional.empty();
    }
}
