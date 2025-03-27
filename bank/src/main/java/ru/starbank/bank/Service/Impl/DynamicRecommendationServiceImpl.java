package ru.starbank.bank.Service.Impl;

import org.springframework.stereotype.Service;
import ru.starbank.bank.Model.DynamicRecommendation;
import ru.starbank.bank.Repository.DynamicRecommendationsRepository;
import ru.starbank.bank.Service.DynamicRecommendationService;

import java.util.List;

@Service
public class DynamicRecommendationServiceImpl implements DynamicRecommendationService {

    private final DynamicRecommendationsRepository dynamicRecommendationsRepository;

    public DynamicRecommendationServiceImpl(DynamicRecommendationsRepository dynamicRecommendationsRepository) {
        this.dynamicRecommendationsRepository = dynamicRecommendationsRepository;
    }

    @Override
    public DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation) {
        return dynamicRecommendationsRepository.save(recommendation);
    }

    @Override
    public List<DynamicRecommendation> getAllDynamicRecommendations() {
        return dynamicRecommendationsRepository.findAll();
    }

    @Override
    public void deleteDynamicRecommendation(Long recommendationId) {
        dynamicRecommendationsRepository.deleteById(recommendationId);
    }
}
