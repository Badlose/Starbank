package ru.starbank.bank.service.ruleSets.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.Impl.RecommendationUserOfRuleServiceImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class RecommendationActiveUserOfRuleServiceImplUnitTest {
    @Mock
    private TransactionsRepository repository;
    @InjectMocks
    private RecommendationUserOfRuleServiceImpl service;

    @Test
    void shouldCorrectlyCheckRuleActiveUserOf() {
        int result = 5;
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        Rule givenRule = new Rule("USER_OF", List.of("DEBIT"), true);
        String productType = givenRule.getArguments().get(0);

        when(repository.countTransactionsByUserIdProductType(userId, productType))
                .thenReturn(result);

        boolean expectedResult = service.check(userId, givenRule);

        assertTrue(expectedResult);
        verify(repository, times(1))
                .countTransactionsByUserIdProductType(userId, productType);
    }

    @Test
    void shouldReturnFalseCheckRuleActiveUserOf() {
        int result = 0;
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        Rule givenRule = new Rule("USER_OF", List.of("DEBIT"), true);
        String productType = givenRule.getArguments().get(0);

        when(repository.countTransactionsByUserIdProductType(userId, productType))
                .thenReturn(result);

        boolean expectedResult = service.check(userId, givenRule);

        assertFalse(expectedResult);
        verify(repository, times(1))
                .countTransactionsByUserIdProductType(userId, productType);
    }
}
