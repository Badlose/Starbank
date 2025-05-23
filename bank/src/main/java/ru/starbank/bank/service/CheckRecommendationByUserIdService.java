package ru.starbank.bank.service;

import ru.starbank.bank.model.DynamicRecommendation;

import java.util.UUID;

public interface CheckRecommendationByUserIdService {

    boolean checkDynamicRecommendation(UUID userId, DynamicRecommendation recommendation);

}
