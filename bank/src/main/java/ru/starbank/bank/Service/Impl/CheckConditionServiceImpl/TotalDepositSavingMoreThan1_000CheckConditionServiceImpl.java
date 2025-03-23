package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

@Qualifier("RuleSetOneRuleThree")
@Component
public class TotalDepositSavingMoreThan1_000CheckConditionServiceImpl implements CheckConditionService {

    private final RecommendationsRepository recommendationsRepository;

    public TotalDepositSavingMoreThan1_000CheckConditionServiceImpl(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {
        return recommendationsRepository.TotalDepositSavingMoreThan1_000(userId);
    }
}
