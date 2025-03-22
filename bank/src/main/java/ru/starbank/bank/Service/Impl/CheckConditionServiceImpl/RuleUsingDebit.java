package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

public class RuleUsingDebit implements CheckConditionService {

    private final RecommendationsRepository recommendationsRepository;

    public RuleUsingDebit(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {
        return recommendationsRepository.UsingDebit(userId);
    }

}
