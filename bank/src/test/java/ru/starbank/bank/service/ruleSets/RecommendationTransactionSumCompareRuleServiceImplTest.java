package ru.starbank.bank.service.ruleSets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.starbank.bank.configuration.RecommendationsDataSourceConfiguration;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.Impl.RecommendationTransactionSumCompareRuleServiceImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecommendationTransactionSumCompareRuleServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationTransactionSumCompareRuleServiceImplTest.class);

    private RecommendationsDataSourceConfiguration dataSourceConfiguration;
    private RecommendationTransactionSumCompareRuleServiceImpl ruleService;
    private TransactionsRepository repository;


    public RecommendationTransactionSumCompareRuleServiceImplTest() {
        this.dataSourceConfiguration = new RecommendationsDataSourceConfiguration();
        this.repository = new TransactionsRepository(
                dataSourceConfiguration.recommendationsJdbcTemplate
                        (dataSourceConfiguration.recommendationsDataSource
                                ("jdbc:h2:file:./transactionTests")));
        this.ruleService = new RecommendationTransactionSumCompareRuleServiceImpl(repository);
    }

    @BeforeEach
    public void clear() {
        ruleService = new RecommendationTransactionSumCompareRuleServiceImpl(repository);
    }

    @Test
    public void shouldCorrectlyCheckRuleTransactionSumCompare() {
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        Rule givenRule = new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">=", "50000"), true);

        boolean expectedResult = ruleService.check(userId, givenRule);

        assertTrue(expectedResult);
    }

    @Test
    public void shouldReturnFalseWithIncorrectUserIdCheckRuleTransactionSumCompare() {
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b2cb-76c49409546b");
        Rule givenRule = new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000"), true);

        boolean expectedResult = ruleService.check(userId, givenRule);

        assertFalse(expectedResult);
    }


    @Test
    public void shouldThrowExceptionThenUserIdIsNull() {
        UUID userId = null;
        Rule givenRule = new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000"), true);

        assertThrows(Exception.class,
                () -> repository.compareTransactionSumByUserIdProductType(
                        userId,
                        givenRule.getArguments().get(0),
                        givenRule.getArguments().get(1)));
    }

    @Test
    public void shouldThrowExceptionThenRuleIsNull() {
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b2cb-76c49409546b");
        Rule givenRule = null;

        assertThrows(Exception.class,
                () -> repository.compareTransactionSumByUserIdProductType(
                        userId,
                        givenRule.getArguments().get(0),
                        givenRule.getArguments().get(1)));
    }
}