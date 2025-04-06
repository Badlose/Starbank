package ru.starbank.bank.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.CheckRuleService;
import ru.starbank.bank.service.RecommendationRuleService;

import java.util.List;
import java.util.UUID;

@Qualifier("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
@Component
public class RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl implements RecommendationRuleService {

    private final TransactionsRepository repository;

    private final CheckRuleService ruleService;

    private static final Logger logger = LoggerFactory.getLogger(
            RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl.class);


    public RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl(TransactionsRepository repository, CheckRuleService ruleService) {
        this.repository = repository;
        this.ruleService = ruleService;
    }

    @Override
    public boolean check(UUID userId, Rule rule) {

        List<String> arguments = rule.getArguments();
        String productType = arguments.get(0);
        String comparison = rule.getArguments().get(1);
        int result;

        ruleService.checkRule(rule);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            result = repository.compareTransactionSumByUserIdProductTypeDepositWithdraw(userId, productType, comparison);
        } finally {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeNanos();
            logger.info("Query executed in {} ns for user {} and product type {} and comparison symbol {}.",
                    executionTime, userId, productType, comparison);
        }

        return rule.isNegate() ? result == 1 : result == 0;
    }

}
