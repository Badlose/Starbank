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

@Qualifier("TRANSACTION_SUM_COMPARE")
@Component
public class RecommendationTransactionSumCompareRuleServiceImpl implements RecommendationRuleService {

    private final TransactionsRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(RecommendationTransactionSumCompareRuleServiceImpl.class);


    public RecommendationTransactionSumCompareRuleServiceImpl(TransactionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean check(UUID userId, Rule rule) {

        List<String> arguments = rule.getArguments();
        String productType = arguments.get(0);
        String transactionType = arguments.get(1);
        boolean result;
        int threshold = Integer.parseInt(arguments.get(3));
        String operator = arguments.get(2);
        int value;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            value = repository.compareTransactionSumByUserIdProductType(userId, productType, transactionType);
        } finally {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeNanos();
            logger.info("Query executed in {} ns for user {} and product type {} and transaction type {}.",
                    executionTime, userId, productType, transactionType);
        }

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
