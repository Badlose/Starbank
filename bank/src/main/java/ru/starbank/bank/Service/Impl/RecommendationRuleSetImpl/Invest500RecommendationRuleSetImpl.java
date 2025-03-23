package ru.starbank.bank.Service.Impl.RecommendationRuleSetImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.CheckConditionService;
import ru.starbank.bank.Service.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RecommendationRuleSetImpl implements RecommendationRuleSet {

    private final List<CheckConditionService> conditionServices;

    private static final Logger logger = LoggerFactory.getLogger(Invest500RecommendationRuleSetImpl.class);

    public Invest500RecommendationRuleSetImpl(@Qualifier("DebitUsing") CheckConditionService checkRuleOne,
                                              @Qualifier("InvestNotUsing") CheckConditionService checkRuleTwo,
                                              @Qualifier("SavingDepositMoreThan1_000") CheckConditionService checkRulThree) {
        this.conditionServices = List.of(checkRuleOne, checkRuleTwo, checkRulThree);
    }

    private static final String ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private static final String NAME = "Invest500";
    private static final String TEXT = """
            Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! \
            Воспользуйтесь налоговыми льготами и начните инвестировать с умом. \
            Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. \
            Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. \
            Откройте ИИС сегодня и станьте ближе к финансовой независимости!""";

    private static final Recommendation RECOMMENDATION = new Recommendation(
            UUID.fromString(ID),
            NAME,
            TEXT
    );

    @Override
    public Optional<Recommendation> check(UUID userId) {

        boolean checkCondition = conditionServices.stream()
                .allMatch(r -> r.checkCondition(userId));

        if (checkCondition) {
            logger.info("The Invest 500 product is suitable for the user {}", userId);
            return Optional.of(RECOMMENDATION);
        }
        logger.info("The Invest 500 product was not suitable for the user {}", userId);
        return Optional.empty();
    }

}


