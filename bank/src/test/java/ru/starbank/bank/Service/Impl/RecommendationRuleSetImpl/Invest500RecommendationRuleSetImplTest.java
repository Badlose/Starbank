package ru.starbank.bank.Service.Impl.RecommendationRuleSetImpl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class Invest500RecommendationRuleSetImplTest {

    @Mock
    private CheckConditionService conditionService;

    @InjectMocks
    private Invest500RecommendationRuleSetImpl invest500RecommendationRuleSet;

    private static final String ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";

    private static final String NAME = "Invest500";
    private static final String TEXT = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private static  Recommendation recommendation = new Recommendation(
            UUID.fromString(ID),
            NAME,
            TEXT
    );

    @Test
    public void checkTestGood() {
        boolean bool = true;

        Mockito.when(conditionService.checkCondition(UUID.fromString(ID))).thenReturn(bool);
        Optional<Recommendation> extendResult = Optional.of(recommendation);

        Optional<Recommendation> actualResult = invest500RecommendationRuleSet.check(UUID.fromString(ID));

        Assertions.assertNotNull(actualResult);
        Assertions.assertEquals(actualResult, extendResult);
    }

    @Test
    public void checkTestFail() {
        boolean bool = false;

        Mockito.when(conditionService.checkCondition(UUID.fromString(ID))).thenReturn(bool);
        Optional<Recommendation> extendResult = Optional.empty();

        Optional<Recommendation> actualResult = invest500RecommendationRuleSet.check(UUID.fromString(ID));

        Assertions.assertNotNull(actualResult);
        Assertions.assertEquals(actualResult, extendResult);
    }
}
