package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.service.RecommendationRuleSet;

import java.util.UUID;

@Qualifier("TRANSACTION_SUM_COMPARE")
@Component
public class RecommendationTransactionSumCompareRuleSetImpl implements RecommendationRuleSet {

    @Override
    public DynamicRecommendation check(UUID userId) {
        return null;
    }
}
