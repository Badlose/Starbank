package ru.starbank.bank.Service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Controller.RecommendationController;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.RecommendationRuleSet;
import ru.starbank.bank.Service.RecommendationService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationServiceImpl implements RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {

        //List<Recommendation> //бины

        return Optional.empty();
    }


    public List<Recommendation> getRecommendationsWithLogging(UUID userId) {
        logger.info("Запрос на получение рекомендаций для пользователя с ID: {}", userId);
        List<Recommendation> recommendations = getRecommendation(userId).orElseGet(Collections::emptyList);
        if (!recommendations.isEmpty()) {
            logger.info("Рекомендации найдены для пользователя с ID: {}", userId);
        } else {
            logger.info("Рекомендации не найдены для пользователя с ID: {}", userId);
        }
        return recommendations;
    }

    @Override
    public Integer getAmount(UUID userId) {

        return 0;
    }

}
