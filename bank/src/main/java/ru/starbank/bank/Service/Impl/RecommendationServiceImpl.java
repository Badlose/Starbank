package ru.starbank.bank.Service.Impl;

import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.Impl.RecommendationRuleSetImpl.RecommendationRuleSetOneImpl;
import ru.starbank.bank.Service.RecommendationRuleSet;
import ru.starbank.bank.Service.RecommendationService;
import ru.starbank.bank.Service.Impl.RecommendationRuleSetImpl.RecommendationRuleSetTwoImpl;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationServiceImpl implements RecommendationService {

    private RecommendationRuleSet ruleSet;

    public RecommendationServiceImpl(RecommendationRuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

//    RecommendationRuleSet ruleSet2;
//    RecommendationRuleSet ruleSet3;

    @Override
    public Optional<Recommendation> getRecommendation(UUID userId) {

        ruleSet.check(userId); //бины

        return Optional.empty();
    }

}
