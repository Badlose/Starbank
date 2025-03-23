package ru.starbank.bank.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.RecommendationRuleSet;
import ru.starbank.bank.Service.RecommendationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationServiceImpl(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    @Override
       public Optional<List<Recommendation>> getRecommendation(UUID userId) {

        List<Recommendation> recommendations = ruleSets.stream()
                .map(r -> r.check(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return recommendations.isEmpty() ? Optional.empty() : Optional.of(recommendations); //нельзя null
    }

    @Override
    public Integer getAmount(UUID userId) {

        return 0;
    }

}
