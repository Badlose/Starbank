package ru.starbank.bank.Service.Impl.RecommendationRuleSetImpl;

import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.Impl.CheckConditionServiceImpl.RuleUsingDebit;
import ru.starbank.bank.Service.RecommendationRuleSet;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRuleSetOneImpl implements RecommendationRuleSet {

    private RuleUsingDebit ruleUsingDebit;   // возможно через интерфейс

    @Override
    public Optional<Recommendation> check(UUID userId) {

        ruleUsingDebit.checkCondition(userId);

        return Optional.empty();
    }

}
