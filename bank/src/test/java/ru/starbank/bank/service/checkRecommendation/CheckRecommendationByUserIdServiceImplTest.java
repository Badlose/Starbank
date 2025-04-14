package ru.starbank.bank.service.checkRecommendation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import ru.starbank.bank.configuration.CacheConfigurations;
import ru.starbank.bank.configuration.RecommendationsDataSourceConfiguration;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.CheckRuleService;
import ru.starbank.bank.service.Impl.*;
import ru.starbank.bank.service.RecommendationRuleService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckRecommendationByUserIdServiceImplTest {
    private RecommendationsDataSourceConfiguration dataSourceConfiguration = new RecommendationsDataSourceConfiguration();
    private TransactionsRepository repository = new TransactionsRepository(
            dataSourceConfiguration.recommendationsJdbcTemplate(
                    dataSourceConfiguration.recommendationsDataSource("jdbc:h2:file:./transactionTests")));


    private RecommendationUserOfRuleServiceImpl ruleUserOf = new RecommendationUserOfRuleServiceImpl(repository);
    private RecommendationActiveUserOfRuleServiceImpl ruleActiveUserOf = new RecommendationActiveUserOfRuleServiceImpl(repository);
    private RecommendationTransactionSumCompareRuleServiceImpl ruleTransactionSumCompare = new RecommendationTransactionSumCompareRuleServiceImpl (repository);
    private RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl  ruleTransactionSumCompareDepositWithdraw = new RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl(repository);

    private CheckRuleService ruleService = new CheckRuleServiceImpl();


    private CheckRecommendationByUserIdServiceImpl recommendationService = new CheckRecommendationByUserIdServiceImpl(
            ruleUserOf,
            ruleActiveUserOf,
            ruleTransactionSumCompare,
            ruleTransactionSumCompareDepositWithdraw,
            ruleService);


    private static final String INVEST500ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private static final String INVEST500NAME = "Invest500";
    private static final String INVEST500TEXT = "INVEST500TEXT";

    private static final String TOPSAVINGID = "59efc529-2fff-41af-baff-90ccd7402925";
    private static final String TOPSAVINGNAME = "TopSaving";
    private static final String TOPSAVINGTEXT = "TOPSAVINGTEXT";

    private static final String SIMPLELOANID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private static final String SIMPLELOANNAME = "Простой кредит";
    private static final String SIMPLELOANTEXT = "SIMPLELOANTEXT";

    private static final List<Rule> INVEST500RULES = new ArrayList<>(List.of(
            new Rule("USER_OF", List.of("DEBIT"), true),
            new Rule("USER_OF", List.of("INVEST"), false),
            new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000"), true)
    ));
    private static final List<Rule> TOPSAVINGRULES = new ArrayList<>(List.of(
            new Rule("USER_OF", List.of("DEBIT"), true),
            new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">=", "50000", "OR"), true),
            new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000", "OR"), true),
            new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true) // +
    ));
    private static final List<Rule> SIMPLELOANRULES = new ArrayList<>(List.of(
            new Rule("USER_OF", List.of("CREDIT"), false),
            new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true),
            new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "WITHDRAW", ">", "100000"), true)
    ));

    private static DynamicRecommendation invest500DynamicRecommendation = new DynamicRecommendation(
            INVEST500NAME, UUID.fromString(INVEST500ID), INVEST500TEXT, INVEST500RULES);
    private static DynamicRecommendation topSavingDynamivRecommendation = new DynamicRecommendation(
            TOPSAVINGNAME, UUID.fromString(TOPSAVINGID), TOPSAVINGTEXT, TOPSAVINGRULES);
    private static DynamicRecommendation simpleLoan = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, SIMPLELOANRULES);

    private static UUID userIdInvest500 = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
    private static UUID userIdTopSaving = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
    private static UUID userIdSimpleLoan = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

    @Test
    public void shouldCorrectlyCheckDynamicRecommendation() {

        boolean expectedResult = recommendationService.checkDynamicRecommendation(userIdInvest500, invest500DynamicRecommendation);

        assertTrue(expectedResult);
    }
}
