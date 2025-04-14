package ru.starbank.bank.service.checkRecommendation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.service.CheckRecommendationService;
import ru.starbank.bank.service.Impl.CheckRecommendationByUserIdServiceImpl;
import ru.starbank.bank.service.RecommendationRuleService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckRecommendationByUserIdServiceImplUnitTest {
    @Mock
    private RecommendationRuleService ruleService;
    @Mock
    private CheckRecommendationService checkRecommendationService;
    @InjectMocks
    private CheckRecommendationByUserIdServiceImpl service;

    private static final UUID INVEST500ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private static final Rule userOfRule = new Rule("USER_OF", List.of("DEBIT"), true);

    @Test
    void shouldCorrectlyCheckDynamicRecommendation() {

        when(ruleService.check(INVEST500ID, userOfRule)).thenReturn(true);

        boolean expectedResult = ruleService.check(INVEST500ID, userOfRule);

        assertTrue(expectedResult);
        verify(ruleService, times(1)).check(INVEST500ID, userOfRule);
    }

    @Test
    void shouldReturnFalseCheckDynamicRecommendation() {

        when(ruleService.check(INVEST500ID, userOfRule)).thenReturn(false);

        boolean expectedResult = ruleService.check(INVEST500ID, userOfRule);

        assertFalse(expectedResult);
        verify(ruleService, times(1)).check(INVEST500ID, userOfRule);
    }
}
