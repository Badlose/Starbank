package ru.starbank.bank.service;

import ru.starbank.bank.model.Rule;

import java.util.UUID;

public interface RecommendationRuleSetService {
    boolean check(UUID userId, Rule rule);
}
