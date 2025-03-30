package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.RecommendationRuleSet;

import java.util.UUID;

@Qualifier("USER_OF")
@Component
public class RecommendationUserOfRuleSetImpl implements RecommendationRuleSet {

    private final TransactionsRepository repository;

    public RecommendationUserOfRuleSetImpl(TransactionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean check(UUID userId, Rule rule) {

        if (rule.getQuery().equals("USER_OF")) {
            int result = repository.checkUserOfRule(userId, rule);

            if (rule.isNegate()) {
                return result > 0;
            }
            return result == 0;
        }

        return true;
    }
}
