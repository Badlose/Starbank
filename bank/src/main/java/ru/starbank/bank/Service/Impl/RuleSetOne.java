package ru.starbank.bank.Service.Impl;

import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.RecommendationService;

import java.util.Optional;
import java.util.UUID;

@Component
public class RuleSetOne {

    private static RecommendationsRepository repository;

    public RuleSetOne(RecommendationsRepository repository) {
        this.repository = repository;
    }

    public static Integer getAmount(UUID userId) {
        return repository.getRandomTransactionAmountV1(userId);
    }

    public static Integer getDebitInfo() {
        return repository.getDebitInfo();
    }

    public boolean check() {
        ruleOne.check();
        ruleTwo.check();
        ruleThree.check();

        return true;
    }

//
//    @Override
//    public Optional<Recommendation> getRecommendation(UUID userId) {
//
//        return Optional.empty();
//    }
//
//    @Override
//    public Integer getAmount(UUID userId) {
//        return repository.getRandomTransactionAmount(userId);
//    }

}
