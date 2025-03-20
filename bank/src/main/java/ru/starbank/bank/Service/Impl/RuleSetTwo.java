package ru.starbank.bank.Service.Impl;

import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.RecommendationService;

import java.util.Optional;
import java.util.UUID;

@Component
public class RuleSetTwo {

    private static RecommendationsRepository repository;

    public RuleSetTwo(RecommendationsRepository repository) {
        this.repository = repository;
    }

    public static Integer getAmount(UUID userId) {
        return repository.getRandomTransactionAmountV1(userId);
    }

//    @Override
//    public Optional<Recommendation> getRecommendation(UUID userId) {
//
//        return Optional.empty();
//    }
//
//    @Override
//    public Integer getAmount(UUID userId) {
//        return 0;
//    }
}
