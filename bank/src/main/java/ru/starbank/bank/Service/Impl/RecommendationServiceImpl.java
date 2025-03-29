package ru.starbank.bank.Service.Impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.starbank.bank.Model.DynamicRecommendation;
import ru.starbank.bank.Model.Rule;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Repository.RulesRepository;
import ru.starbank.bank.Service.RecommendationService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationsRepository recommendationsRepository;

    private final RulesRepository rulesRepository;


    public RecommendationServiceImpl(RecommendationsRepository recommendationsRepository,
                                     RulesRepository rulesRepository) {
        this.recommendationsRepository = recommendationsRepository;
        this.rulesRepository = rulesRepository;
    }

    @Override
    @Cacheable(value = "recommendationCache", key = "#userId")
    public List<DynamicRecommendation> getRecommendation(UUID userId) {

        return Collections.emptyList();
    }


    @Override
    @CacheEvict(value = "createNewDynamicRecommendation", allEntries = true) // Очистка кэша при создании новой рекомендации
    public DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation) {
        recommendationsRepository.save(recommendation);
        for (Rule rule : recommendation.getRuleList()) {
            rule.setDynamicRecommendation(recommendation);
            rulesRepository.save(rule);
        }
        return recommendation;
    }

    @Override
    public List<DynamicRecommendation> getAllDynamicRecommendations() {
        return recommendationsRepository.findAll();
    }

    @Override
    @CacheEvict(value = "deleteDynamicRecommendation", allEntries = true) // Очистка кэша при удалении рекомендации
    public HttpStatus deleteDynamicRecommendation(Long id) {
        DynamicRecommendation recommendation = recommendationsRepository.findById(id).orElse(null);

        if (recommendation == null) {
            return HttpStatus.BAD_REQUEST;
        }

        for (Rule e : recommendation.getRuleList()) {
            rulesRepository.deleteById(e.getId());
        }
        recommendationsRepository.deleteById(id);

        return HttpStatus.NO_CONTENT;

    }

}
