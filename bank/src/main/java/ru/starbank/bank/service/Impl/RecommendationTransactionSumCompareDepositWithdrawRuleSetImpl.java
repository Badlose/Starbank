package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.RecommendationRuleSet;

import java.util.UUID;

@Qualifier("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
@Component
public class RecommendationTransactionSumCompareDepositWithdrawRuleSetImpl implements RecommendationRuleSet {

    private final TransactionsRepository repository;

    public RecommendationTransactionSumCompareDepositWithdrawRuleSetImpl(TransactionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean check(UUID userId, Rule rule) {
        if (rule.getQuery().equals("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")) {

            int result = repository.checkTransactionSumCompareDepositWithdrawRule(userId, rule);

            if (rule.isNegate()) {
                return result == 1;
            }

            return result == 0;

        }

        return true;

    }
}
