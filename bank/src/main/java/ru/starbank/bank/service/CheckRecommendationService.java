package ru.starbank.bank.service;

import ru.starbank.bank.model.DynamicRecommendation;

public interface CheckRecommendationService {

    boolean checkRecommendationCorrect(DynamicRecommendation recommendation);

}
