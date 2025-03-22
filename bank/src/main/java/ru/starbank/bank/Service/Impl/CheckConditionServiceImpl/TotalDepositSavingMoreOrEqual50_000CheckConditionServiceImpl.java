package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

public class TotalDepositSavingMoreOrEqual50_000CheckConditionServiceImpl implements CheckConditionService {

    private final RecommendationsRepository recommendationsRepository;

    public TotalDepositSavingMoreOrEqual50_000CheckConditionServiceImpl(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {
        return recommendationsRepository.TotalDepositSavingMoreOrEqual50_000(userId);
    }
}
