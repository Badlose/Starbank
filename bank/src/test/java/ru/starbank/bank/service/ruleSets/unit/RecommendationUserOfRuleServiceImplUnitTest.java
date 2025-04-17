package ru.starbank.bank.service.ruleSets.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.Impl.RecommendationUserOfRuleServiceImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationUserOfRuleServiceImplUnitTest {
    @Mock
    private TransactionsRepository repository;
    @InjectMocks
    private RecommendationUserOfRuleServiceImpl service;

    @Test
    void shouldCorrectlyCheckRuleUserOf() {
        int result = 1;
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
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
    void shouldReturnFalseCheckRuleUserOf() {
        int result = 0;
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
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
