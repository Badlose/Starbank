package ru.starbank.bank.Controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.RecommendationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    public final RecommendationService recommendationService;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);


    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }


    @GetMapping("/user_id/{user_id}")
    public Optional<List<Recommendation>> getRecommendation(@PathVariable UUID user_id) {
        logger.info("Запрос на получение рекомендаций для пользователя с ID: {}", user_id);
        Optional<List<Recommendation>> recommendations = recommendationService.getRecommendation(user_id);
        if (recommendations.isPresent()) {
            logger.info("Рекомендации найдены для пользователя с ID: {}", user_id);
        } else {
            logger.warn("Рекомендации не найдены для пользователя с ID: {}", user_id);
        }
        return recommendations;
    }


    @GetMapping("/amount/{userId}")
    public Integer getAmountTest(
            @Parameter(description = "Идентификатор пользователя (UUID)",
                    schema = @Schema(type = "string", format = "uuid",
                            example = "a1b2c3d4-e5f6-4789-9abc-def012345678"))
            @PathVariable UUID userId) {
        return recommendationService.getAmount(userId);
    }
}