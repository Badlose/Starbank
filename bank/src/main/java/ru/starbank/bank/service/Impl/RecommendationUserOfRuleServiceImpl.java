package ru.starbank.bank.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.RecommendationRuleService;
import ru.starbank.bank.service.RuleService;

import java.util.List;
import java.util.UUID;

@Qualifier("USER_OF")
@Component
public class RecommendationUserOfRuleServiceImpl implements RecommendationRuleService {

    private final TransactionsRepository repository;

    private final RuleService ruleService;

    private static final Logger logger = LoggerFactory.getLogger(RecommendationUserOfRuleServiceImpl.class);


    public RecommendationUserOfRuleServiceImpl(TransactionsRepository repository, RuleService ruleService) {
        this.repository = repository;
        this.ruleService = ruleService;
    }

    @Override
    public boolean check(UUID userId, Rule rule) {

        List<String> arguments = rule.getArguments();
        String productType = arguments.get(0);
        int result;

        ruleService.checkRule(rule);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            result = repository.countTransactionsByUserIdProductType(userId, productType);
        } finally {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();
            logger.info("Query executed in {} ms for user {} and product type {}.",
                    executionTime, userId, productType);
        }

        return rule.isNegate() == (result > 0);
    }

}
