package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.RecommendationRuleSetService;
import ru.starbank.bank.service.RuleService;

import java.util.List;
import java.util.UUID;

@Qualifier("USER_OF")
@Component
public class RecommendationUserOfRuleSetServiceImpl implements RecommendationRuleSetService {

    private final TransactionsRepository repository;

    private final RuleService ruleService;

    public RecommendationUserOfRuleSetServiceImpl(TransactionsRepository repository, RuleService ruleService) {
        this.repository = repository;
        this.ruleService = ruleService;
    }

    @Override
    public boolean check(UUID userId, Rule rule) {

        List<String> arguments = rule.getArguments();
        String productType = arguments.get(0);

        ruleService.checkRule(rule);

        int result = repository.countTransactionsByUserIdProductType(userId, productType);

        return rule.isNegate() ? result > 0 : result == 0;
    }

}
