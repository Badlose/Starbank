package ru.starbank.bank.Service.Impl.RecommendationRuleSetImpl;

import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.Rules.RuleDebitInfo;
import ru.starbank.bank.Service.RecommendationRuleSet;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRuleSetOneImpl implements RecommendationRuleSet {

    private RuleDebitInfo ruleDebitInfo;   // возможно через интерфейс

    @Override
    public Optional<Recommendation> check(UUID userId) {

        ruleDebitInfo.checkDebitInfo(userId);

        return Optional.empty();
    }

}
