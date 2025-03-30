package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.service.RecommendationRuleSet;

import java.util.UUID;

@Qualifier("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
@Component
public class RecommendationTransactionSumCompareDepositWithdrawRuleSetImpl implements RecommendationRuleSet {
    @Override
    @Cacheable(value = "dynamicRecommendationCache", key = "#userId")
    public DynamicRecommendation check(UUID userId) {
        return null;
    }
}
