package ru.starbank.bank.Service.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.starbank.bank.Model.DynamicRecommendation;
import ru.starbank.bank.Model.Rule;
import ru.starbank.bank.Repository.DynamicRecommendationsRepository;
import ru.starbank.bank.Repository.RulesRepository;
import ru.starbank.bank.Service.DynamicRecommendationService;

import java.util.List;

@Service
public class DynamicRecommendationServiceImpl implements DynamicRecommendationService {

    private final DynamicRecommendationsRepository dynamicRecommendationsRepository;

    private final RulesRepository rulesRepository;

    public DynamicRecommendationServiceImpl(DynamicRecommendationsRepository dynamicRecommendationsRepository,
                                            RulesRepository rulesRepository) {
        this.dynamicRecommendationsRepository = dynamicRecommendationsRepository;
        this.rulesRepository = rulesRepository;
    }

    @Override
    public DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation) {
        dynamicRecommendationsRepository.save(recommendation);
        for (Rule rule : recommendation.getRuleList()) {
            rule.setDynamicRecommendation(recommendation);
            rulesRepository.save(rule);
        }
        return recommendation;
    }

    @Override
    public List<DynamicRecommendation> getAllDynamicRecommendations() {
        return dynamicRecommendationsRepository.findAll();
    }

    @Override
    public HttpStatus deleteDynamicRecommendation(Long id) {
        DynamicRecommendation recommendation = dynamicRecommendationsRepository.findById(id).orElse(null);

        if (recommendation == null) {
            return HttpStatus.BAD_REQUEST;
        } else {
            for (Rule e : recommendation.getRuleList()) {
                rulesRepository.deleteById(e.getId());
            }
            dynamicRecommendationsRepository.deleteById(id);

            return HttpStatus.NO_CONTENT;
        }
    }
}
