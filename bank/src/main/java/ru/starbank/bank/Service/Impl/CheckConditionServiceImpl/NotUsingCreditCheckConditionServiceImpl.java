package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

public class NotUsingCreditCheckConditionServiceImpl implements CheckConditionService {

    private final RecommendationsRepository recommendationsRepository;

    public NotUsingCreditCheckConditionServiceImpl(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {
                return recommendationsRepository.NotUsingCredit(userId);
    }
}
