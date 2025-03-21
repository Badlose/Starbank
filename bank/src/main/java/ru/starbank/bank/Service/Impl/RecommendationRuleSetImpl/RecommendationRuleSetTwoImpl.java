package ru.starbank.bank.Service.Impl.RecommendationRuleSetImpl;

import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.RecommendationRuleSet;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRuleSetTwoImpl implements RecommendationRuleSet {

    @Override
    public Optional<Recommendation> check(UUID userId) {

        return Optional.empty();
    }
}
