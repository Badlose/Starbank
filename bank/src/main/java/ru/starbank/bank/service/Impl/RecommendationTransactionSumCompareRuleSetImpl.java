package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.RecommendationRuleSet;

import java.util.UUID;

@Qualifier("TRANSACTION_SUM_COMPARE")
@Component
public class RecommendationTransactionSumCompareRuleSetImpl implements RecommendationRuleSet {


    private final TransactionsRepository repository;

    public RecommendationTransactionSumCompareRuleSetImpl(TransactionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean check(UUID userId, Rule rule) {
        if (rule.getQuery().equals("TRANSACTION_SUM_COMPARE")) {

            int result = repository.checkTransactionSumCompareRule(userId, rule);

            //дописать сравнение с negate
        }

        return true;
    }
}
