package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

public class TotalDepositDebitMoreOrEqual50_000CheckConditionServiceImpl
        implements CheckConditionService {

    private final RecommendationsRepository recommendationsRepository;

    public TotalDepositDebitMoreOrEqual50_000CheckConditionServiceImpl(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {
        return recommendationsRepository.TotalDepositDebitMoreOrEqual50_000(userId);
    }
}
