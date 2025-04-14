package ru.starbank.bank.service.ruleSets.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.Impl.RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationTransactionSumCompareDepositWithdrawRuleServiceImplUnitTest {
    @Mock
    private TransactionsRepository repository;
    @InjectMocks
    private RecommendationTransactionSumCompareDepositWithdrawRuleServiceImpl service;

    @Test
    void shouldCorrectlyCheckRuleTransactionSumCompare() {
        int result = 1;
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        Rule givenRule = new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true);
        String productType = givenRule.getArguments().get(0);
        String comparison = givenRule.getArguments().get(1);

        when(repository.compareTransactionSumByUserIdProductTypeDepositWithdraw(userId, productType, comparison))
                .thenReturn(result);

        boolean expectedResult = service.check(userId, givenRule);

        assertTrue(expectedResult);
        verify(repository, times(1))
                .compareTransactionSumByUserIdProductTypeDepositWithdraw(userId, productType, comparison);
    }

    @Test
    void shouldReturnFalseCheckRuleTransactionSumCompare() {
        int result = 0;
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        Rule givenRule = new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true);
        String productType = givenRule.getArguments().get(0);
        String comparison = givenRule.getArguments().get(1);

        when(repository.compareTransactionSumByUserIdProductTypeDepositWithdraw(userId, productType, comparison))
                .thenReturn(result);

        boolean expectedResult = service.check(userId, givenRule);

        assertFalse(expectedResult);
        verify(repository, times(1))
                .compareTransactionSumByUserIdProductTypeDepositWithdraw(userId, productType, comparison);
    }
}
