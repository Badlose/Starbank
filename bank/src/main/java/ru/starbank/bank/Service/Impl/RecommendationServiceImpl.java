package ru.starbank.bank.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.DynamicRecommendation;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.RecommendationRuleSet;
import ru.starbank.bank.Service.RecommendationService;
import ru.starbank.bank.Service.RuleService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private final List<RecommendationRuleSet> ruleSets;

    RuleService ruleService;

    public RecommendationServiceImpl(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }


    @Override
    public List<Recommendation> getRecommendation(UUID userId) {
//получить все Динамические
//        проверить на все Динамические
//        проверить на все Статические
//        вернуть результат

        return ruleSets.stream()
                .map(r -> r.check(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation) {
        return ruleService.createNewDynamicRecommendation(recommendation);
    }

    @Override
    public List<DynamicRecommendation> getAllDynamicRecommendations() {
        return ruleService.getAllDynamicRecommendations();
    }

    @Override
    public void deleteDynamicRecommendation(Long recommendationId) {
        ruleService.deleteDynamicRecommendation(recommendationId);
    }

}
