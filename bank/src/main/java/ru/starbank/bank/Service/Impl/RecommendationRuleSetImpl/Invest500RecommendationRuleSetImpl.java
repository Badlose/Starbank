package ru.starbank.bank.Service.Impl.RecommendationRuleSetImpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;
import ru.starbank.bank.Service.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RecommendationRuleSetImpl implements RecommendationRuleSet {

    private final RecommendationsRepository recommendationsRepository;

    private final List<CheckConditionService> conditionServices;

    public Invest500RecommendationRuleSetImpl(RecommendationsRepository recommendationsRepository,
                                              @Qualifier("RuleSetOneRuleOne") CheckConditionService checkRuleOne,
                                              @Qualifier("RuleSetOneRuleTwo") CheckConditionService checkRuleTwo,
                                              @Qualifier("RuleSetOneRuleThree") CheckConditionService checkRulThree) {
        this.recommendationsRepository = recommendationsRepository;
        this.conditionServices = List.of(checkRuleOne, checkRuleTwo, checkRulThree);
    }


    @Override
    public Optional<Recommendation> check(UUID userId) {

        String id = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
        String name = "Invest500";
        String TEXT = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

        Recommendation recommendation = new Recommendation(
                UUID.fromString(id),
                name,
                TEXT
        );

        boolean checkCondition = conditionServices.stream()
                .allMatch(r -> r.checkCondition(userId));

        if (checkCondition) {
            return Optional.of(recommendation);
        }
        return Optional.empty();
    }

}


