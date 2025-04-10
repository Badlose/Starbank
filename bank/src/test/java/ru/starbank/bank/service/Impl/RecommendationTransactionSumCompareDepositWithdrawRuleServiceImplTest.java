package ru.starbank.bank.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.starbank.bank.configuration.RecommendationsDataSourceConfiguration;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class RecommendationTransactionSumCompareDepositWithdrawRuleServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationTransactionSumCompareDepositWithdrawRuleServiceImplTest.class);

    private RecommendationsDataSourceConfiguration dataSourceConfiguration;
    private RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl ruleService;
    private TransactionsRepository repository;


    public RecommendationTransactionSumCompareDepositWithdrawRuleServiceImplTest() {
        this.dataSourceConfiguration = new RecommendationsDataSourceConfiguration();
        this.repository = new TransactionsRepository(
                dataSourceConfiguration.recommendationsJdbcTemplate
                        (dataSourceConfiguration.recommendationsDataSource
                                ("jdbc:h2:file:./transactionTests")));
        this.ruleService = new RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl(repository);
    }

    @BeforeEach
    public void clear() {
        ruleService = new RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl(repository);
    }

    @Test
    public void shouldCorrectlyCheckRuleTransactionSumCompare() {

        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        Rule givenRule = new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true);
        int count = repository.countTransactionsByUserIdProductType(userId, givenRule.getArguments().get(0));

        boolean expectedResult = ruleService.check(userId, givenRule);
        logger.info("count = {}", count);

        assertTrue(expectedResult);
    }

    @Test
    public void shouldReturnFalseWithIncorrectUserIdCheckRuleTransactionSumCompare() {

        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b2cb-76c49409546b");
        Rule givenRule = new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true);
        int count = repository.countTransactionsByUserIdProductType(userId, givenRule.getArguments().get(0));
        logger.info("count = {}", count);

        boolean expectedResult = ruleService.check(userId, givenRule);

        assertFalse(expectedResult);
    }
}