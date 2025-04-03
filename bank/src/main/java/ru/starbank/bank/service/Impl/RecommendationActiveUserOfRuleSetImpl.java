package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.RecommendationRuleSet;

import java.util.List;
import java.util.UUID;

@Qualifier("ACTIVE_USER_OF")
@Component
public class RecommendationActiveUserOfRuleSetImpl implements RecommendationRuleSet {

    private final TransactionsRepository repository;

    public RecommendationActiveUserOfRuleSetImpl(TransactionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean check(UUID userId, Rule rule) {

        List<String> arguments = rule.getArguments();
        String productType = arguments.get(0);

        int result = repository.countTransactionsByUserIdProductType(userId, productType);

        return rule.isNegate() == (result >= 5);

    }
}