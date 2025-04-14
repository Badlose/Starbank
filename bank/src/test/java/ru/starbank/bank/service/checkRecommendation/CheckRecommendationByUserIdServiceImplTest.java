package ru.starbank.bank.service.checkRecommendation;

import org.junit.jupiter.api.Test;
import ru.starbank.bank.configuration.RecommendationsDataSourceConfiguration;
import ru.starbank.bank.exceptions.*;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.CheckRuleService;
import ru.starbank.bank.service.Impl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CheckRecommendationByUserIdServiceImplTest {
    private RecommendationsDataSourceConfiguration dataSourceConfiguration = new RecommendationsDataSourceConfiguration();
    private TransactionsRepository repository = new TransactionsRepository(
            dataSourceConfiguration.recommendationsJdbcTemplate(
                    dataSourceConfiguration.recommendationsDataSource("jdbc:h2:file:./transactionTests")));


    private RecommendationUserOfRuleServiceImpl ruleUserOf =
            new RecommendationUserOfRuleServiceImpl(repository);
    private RecommendationActiveUserOfRuleServiceImpl ruleActiveUserOf =
            new RecommendationActiveUserOfRuleServiceImpl(repository);
    private RecommendationTransactionSumCompareRuleServiceImpl ruleTransactionSumCompare =
            new RecommendationTransactionSumCompareRuleServiceImpl(repository);
    private RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl ruleTransactionSumCompareDepositWithdraw =
            new RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl(repository);

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

    private static final List<Rule> BAD_QUERY_RULE = new ArrayList<>(List.of(
            new Rule("BAD_RULE", List.of("CREDIT"), false)
    ));
    private static final List<Rule> NULL_QUERY_RULE = new ArrayList<>(List.of(
            new Rule(null, List.of("CREDIT"), false)
    ));
    private static final List<Rule> EMPTY_ARGUMENTS_RULE = new ArrayList<>(List.of(
            new Rule("USER_OF", List.of(), false)
    ));
    private static final List<Rule> INVALID_PRODUCT_TYPE_RULE = new ArrayList<>(List.of(
            new Rule("USER_OF", List.of("IVALID"), false)
    ));
    private static final List<Rule> INVALID_COMPARISON_SYMBOL_RULE_DEPOSIT_WITHDRAW = new ArrayList<>(List.of(
            new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", "DROP DATABASE;"), true)
    ));
    private static final List<Rule> INVALID_COMPARISON_SYMBOL_RULE_SUM_COMPARE = new ArrayList<>(List.of(
            new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", "DROP DATABASE;", "50000", "OR"), true)
    ));
    private static final List<Rule> INCORRECT_TRANSACTION_TYPE_RULE = new ArrayList<>(List.of(
            new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "INCORRECT", ">", "100000"), true)
    ));
    private static final List<Rule> INCORRECT_COMPARISON_NUMBER_RULE = new ArrayList<>(List.of(
            new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">", "INCORRECT"), true)
    ));
    private static final List<Rule> INCORRECT_OR_RULE = new ArrayList<>(List.of(
            new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">=", "50000", "not or symbol"), true)
    ));
    private static final List<Rule> THREE_ARGUMENTS_RULE = new ArrayList<>(List.of(
            new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">="), true)
    ));
    private static final List<Rule> MANY_ARGUMENTS_RULE = new ArrayList<>(List.of(
            new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">=", "DEBIT", "DEPOSIT", ">="), true)
    ));

    private static DynamicRecommendation invest500DynamicRecommendation = new DynamicRecommendation(
            INVEST500NAME, UUID.fromString(INVEST500ID), INVEST500TEXT, INVEST500RULES);
    private static DynamicRecommendation topSavingDynamicRecommendation = new DynamicRecommendation(
            TOPSAVINGNAME, UUID.fromString(TOPSAVINGID), TOPSAVINGTEXT, TOPSAVINGRULES);
    private static DynamicRecommendation simpleLoanDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, SIMPLELOANRULES);
    private static DynamicRecommendation badQueryDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, BAD_QUERY_RULE);
    private static DynamicRecommendation nullQueryDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, NULL_QUERY_RULE);
    private static DynamicRecommendation emptyArgumentsDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, EMPTY_ARGUMENTS_RULE);
    private static DynamicRecommendation invalidProductTypeDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, INVALID_PRODUCT_TYPE_RULE);
    private static DynamicRecommendation invalidComparisonSymbolRuleDepositWithdrawDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, INVALID_COMPARISON_SYMBOL_RULE_DEPOSIT_WITHDRAW);
    private static DynamicRecommendation invalidComparisonSymbolRuleSumCompareDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, INVALID_COMPARISON_SYMBOL_RULE_SUM_COMPARE);
    private static DynamicRecommendation incorrectTransactionTypeDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, INCORRECT_TRANSACTION_TYPE_RULE);
    private static DynamicRecommendation incorrectComparisonNumberDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, INCORRECT_COMPARISON_NUMBER_RULE);
    private static DynamicRecommendation incorrectOrRuleDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, INCORRECT_OR_RULE);
    private static DynamicRecommendation threeArgumentsRuleDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, THREE_ARGUMENTS_RULE);
    private static DynamicRecommendation manyArgumentsRuleDynamicRecommendation = new DynamicRecommendation(
            SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, MANY_ARGUMENTS_RULE);

    private static UUID userIdInvest500 = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
    private static UUID userIdTopSaving = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
    private static UUID userIdSimpleLoan = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");
    private static UUID badUserId = UUID.fromString("aa515076-5d8a-44be-930e-8d4fcb79f42d");

    @Test
    public void shouldCorrectlyCheckDynamicRecommendationInvest500() {
        boolean expectedResult = recommendationService.
                checkDynamicRecommendation(userIdInvest500, invest500DynamicRecommendation);
        assertTrue(expectedResult);
    }

    @Test
    public void shouldCorrectlyCheckDynamicRecommendationTopSaving() {
        boolean expectedResult = recommendationService.
                checkDynamicRecommendation(userIdTopSaving, topSavingDynamicRecommendation);
        assertTrue(expectedResult);
    }

    @Test
    public void shouldCorrectlyCheckDynamicRecommendationSimpleLoan() {
        boolean expectedResult = recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, simpleLoanDynamicRecommendation);
        assertTrue(expectedResult);
    }

    @Test
    public void shouldReturnFalseWithBadUserIdCheckDynamicRecommendationInvest500() {
        boolean expectedResult = recommendationService.
                checkDynamicRecommendation(badUserId, invest500DynamicRecommendation);
        assertFalse(expectedResult);
    }

    @Test
    public void shouldReturnFalseWithBadUserIdCheckDynamicRecommendationTopSaving() {
        boolean expectedResult = recommendationService.
                checkDynamicRecommendation(badUserId, topSavingDynamicRecommendation);
        assertFalse(expectedResult);
    }

    @Test
    public void shouldReturnFalseWithBadUserIdCheckDynamicRecommendationSimpleLoan() {
        boolean expectedResult = recommendationService.
                checkDynamicRecommendation(badUserId, simpleLoanDynamicRecommendation);
        assertFalse(expectedResult);
    }

    @Test
    public void shouldThrowSqlRequestExceptionWithNullUserIdCheckDynamicRecommendationInvest500() {
        assertThrows(SqlRequestException.class, () -> recommendationService.
                checkDynamicRecommendation(null, invest500DynamicRecommendation));
    }

    @Test
    public void shouldThrowSqlRequestExceptionWithNullUserIdCheckDynamicRecommendationTopSaving() {
        assertThrows(SqlRequestException.class, () -> recommendationService.
                checkDynamicRecommendation(null, topSavingDynamicRecommendation));
    }

    @Test
    public void shouldThrowSqlRequestExceptionWithNullUserIdCheckDynamicRecommendationSimpleLoan() {
        assertThrows(SqlRequestException.class, () -> recommendationService.
                checkDynamicRecommendation(null, simpleLoanDynamicRecommendation));
    }

    @Test
    public void shouldThrowQueryNullPointerExceptionCheckDynamicRecommendationSimpleLoanWhenQueryIsNull() {
        assertThrows(QueryNullPointerException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, nullQueryDynamicRecommendation));
    }

    @Test
    public void shouldThrowIncorrectRuleQueryValueExceptionCheckDynamicRecommendationSimpleLoan() {
        assertThrows(IncorrectRuleQueryValueException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, badQueryDynamicRecommendation));
    }

    @Test
    public void shouldThrowEmptyRuleArgumentsExceptionCheckDynamicRecommendationSimpleLoanWhenArgumentsIsEmpty() {
        assertThrows(EmptyRuleArgumentsException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, emptyArgumentsDynamicRecommendation));
    }

    @Test
    public void shouldThrowIncorrectProductTypeExceptionWhenProductTypeIsInvalid() {
        assertThrows(IncorrectProductTypeException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, invalidProductTypeDynamicRecommendation));
    }

    @Test
    public void shouldThrowIncorrectComparisonSymbolExceptionWhenComparisonSymbolIsInvalidForRuleTransactionSumCompareDepositWithdraw() {
        assertThrows(IncorrectComparisonSymbolException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, invalidComparisonSymbolRuleDepositWithdrawDynamicRecommendation));
    }

    @Test
    public void shouldThrowIncorrectTransactionTypeExceptionWhenTransactionTypeIsIncorrect() {
        assertThrows(IncorrectTransactionTypeException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, incorrectTransactionTypeDynamicRecommendation));
    }

    @Test
    public void shouldThrowIncorrectComparisonSymbolExceptionWhenComparisonSymbolIsInvalidForRuleTransactionSumCompare() {
        assertThrows(IncorrectComparisonSymbolException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, invalidComparisonSymbolRuleSumCompareDynamicRecommendation));
    }

    @Test
    public void shouldThrowIncorrectNumberForComparisonException() {
        assertThrows(IncorrectNumberForComparisonException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, incorrectComparisonNumberDynamicRecommendation));
    }

    @Test
    public void shouldThrowIncorrectRuleORArgumentException() {
        assertThrows(IncorrectRuleORArgumentException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, incorrectOrRuleDynamicRecommendation));
    }

    @Test
    public void shouldThrowArgumentsSizeExceptionWhenThreeArguments() {
        assertThrows(ArgumentsSizeException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, threeArgumentsRuleDynamicRecommendation));
    }

    @Test
    public void shouldThrowArgumentsSizeExceptionWhenManyArguments() {
        assertThrows(ArgumentsSizeException.class, () -> recommendationService.
                checkDynamicRecommendation(userIdSimpleLoan, manyArgumentsRuleDynamicRecommendation));
    }

}
