package ru.starbank.bank.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.starbank.bank.configuration.RecommendationsDataSourceConfiguration;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationUserOfRuleServiceImplTest {

    private RecommendationsDataSourceConfiguration dataSourceConfiguration;
    private RecommendationUserOfRuleServiceImpl ruleService;
    private TransactionsRepository repository;


    public RecommendationUserOfRuleServiceImplTest() {
        this.dataSourceConfiguration = new RecommendationsDataSourceConfiguration();
        this.repository = new TransactionsRepository(
                dataSourceConfiguration.recommendationsJdbcTemplate
                        (dataSourceConfiguration.recommendationsDataSource
                                ("jdbc:h2:file:./transactionTests")));
        this.ruleService = new RecommendationUserOfRuleServiceImpl(repository);
    }

    @BeforeEach
    public void clear() {
        ruleService = new RecommendationUserOfRuleServiceImpl(repository);
    }

    @Test
    public void shouldCorrectlyCheckRuleUserOf() {

        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        Rule givenRule = new Rule("USER_OF", List.of("DEBIT"), true);

        boolean expectedResult = ruleService.check(userId, givenRule);

        assertTrue(expectedResult);
    }

    @Test
    public void shouldReturnFalseWithIncorrectUserIdCheckRuleUserOf() {

        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f11d");
        Rule givenRule = new Rule("USER_OF", List.of("DEBIT"), true);

        boolean expectedResult = ruleService.check(userId, givenRule);

        assertFalse(expectedResult);
    }
}