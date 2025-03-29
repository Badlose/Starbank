package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.service.RecommendationRuleSet;

import java.util.UUID;

@Qualifier("USER_OF")
@Component
public class RecommendationUserOfRuleSetImpl implements RecommendationRuleSet {

    @Override
    public DynamicRecommendation check(UUID userId) {

        return null;
    }
}
