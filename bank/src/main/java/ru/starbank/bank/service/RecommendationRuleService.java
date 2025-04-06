package ru.starbank.bank.service;

import ru.starbank.bank.model.Rule;

import java.util.UUID;

public interface RecommendationRuleService {

    boolean check(UUID userId, Rule rule);

}
