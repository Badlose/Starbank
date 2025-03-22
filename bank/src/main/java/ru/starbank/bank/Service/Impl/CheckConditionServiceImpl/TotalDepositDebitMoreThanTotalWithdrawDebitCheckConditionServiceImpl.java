package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

public class TotalDepositDebitMoreThanTotalWithdrawDebitCheckConditionServiceImpl implements CheckConditionService {

    private final RecommendationsRepository recommendationsRepository;

    public TotalDepositDebitMoreThanTotalWithdrawDebitCheckConditionServiceImpl(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {
        return recommendationsRepository.TotalDepositDebitMoreThanTotalWithdrawDebit(userId);
    }
}
