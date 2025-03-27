package ru.starbank.bank.Service.Impl;

import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.DynamicRecommendation;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.DynamicRecommendationService;
import ru.starbank.bank.Service.RecommendationRuleSet;
import ru.starbank.bank.Service.RecommendationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationServiceImpl implements RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    private final DynamicRecommendationService dynamicRecommendationService;

    public RecommendationServiceImpl(List<RecommendationRuleSet> ruleSets, DynamicRecommendationService dynamicRecommendationService) {
        this.ruleSets = ruleSets;
        this.dynamicRecommendationService = dynamicRecommendationService;
    }


    @Override
    public List<Recommendation> getRecommendation(UUID userId) {
        return ruleSets.stream()
                .map(r -> r.check(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation) {

        return dynamicRecommendationService.createNewDynamicRecommendation(recommendation);

    }

    @Override
    public List<DynamicRecommendation> getAllDynamicRecommendations() {
        return dynamicRecommendationService.getAllDynamicRecommendations();
    }

    @Override
    public void deleteDynamicRecommendation(Long recommendationId) {
        dynamicRecommendationService.deleteDynamicRecommendation(recommendationId);
    }
}
