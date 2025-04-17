package ru.starbank.bank.service.ruleSets.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.Impl.RecommendationTransactionSumCompareRuleServiceImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationTransactionSumCompareRuleServiceImplUnitTest {
    @Mock
    private TransactionsRepository repository;
    @InjectMocks
    private RecommendationTransactionSumCompareRuleServiceImpl service;

    @Test
    void shouldCorrectlyCheckRuleTransactionSumCompare() {
        int result = 55_000;
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        Rule givenRule = new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">=", "50000"), true);
        String productType = givenRule.getArguments().get(0);
        String transactionType = givenRule.getArguments().get(1);

        when(repository.compareTransactionSumByUserIdProductType(userId, productType, transactionType))
                .thenReturn(result);

        boolean expectedResult = service.check(userId, givenRule);

        assertTrue(expectedResult);
        verify(repository, times(1))
                .compareTransactionSumByUserIdProductType(userId, productType, transactionType);
    }

    @Test
    void shouldReturnFalseCheckRuleTransactionSumCompare() {
        int result = 0;
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        Rule givenRule = new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000"), true);
        String productType = givenRule.getArguments().get(0);
        String transactionType = givenRule.getArguments().get(1);

        when(repository.compareTransactionSumByUserIdProductType(userId, productType, transactionType))
                .thenReturn(result);

        boolean expectedResult = service.check(userId, givenRule);

        assertFalse(expectedResult);
        verify(repository, times(1))
                .compareTransactionSumByUserIdProductType(userId, productType, transactionType);
    }
}
