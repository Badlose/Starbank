package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.RecommendationRuleSetService;
import ru.starbank.bank.service.RuleService;

import java.util.List;
import java.util.UUID;

@Qualifier("TRANSACTION_SUM_COMPARE")
@Component
public class RecommendationTransactionSumCompareRuleSetServiceImpl implements RecommendationRuleSetService {

    private final TransactionsRepository repository;

    private final RuleService ruleService;

    public RecommendationTransactionSumCompareRuleSetServiceImpl(TransactionsRepository repository, RuleService ruleService) {
        this.repository = repository;
        this.ruleService = ruleService;
    }

    @Override
    public boolean check(UUID userId, Rule rule) {

        List<String> arguments = rule.getArguments();
        String productType = arguments.get(0);
        String transactionType = arguments.get(1);

        ruleService.checkRule(rule);
        boolean result = false;

        int threshold = Integer.parseInt(arguments.get(3));
        String operator = arguments.get(2);

        int value = repository.compareTransactionSumByUserIdProductType(userId, productType, transactionType);

        switch (operator) {
            case ">=" -> result = value >= threshold;
            case ">" -> result = value > threshold;
            case "<" -> result = value < threshold;
            case "<=" -> result = value <= threshold;
            case "=" -> result = value == threshold;
            default -> throw new IllegalArgumentException("Недопустимый оператор: " + operator);
        }
        return rule.isNegate() == result;
    }
}
