package ru.starbank.bank.Service.Rules;

import ru.starbank.bank.Repository.RecommendationsRepository;

import java.util.UUID;

public class RuleDebitInfo {
    private final RecommendationsRepository repository;

    public RuleDebitInfo(RecommendationsRepository repository) {
        this.repository = repository;
    }

   // public boolean checkDebitInfo(UUID userId) {
   //     return repository.checkDebitInfo(userId);
   // }
}
