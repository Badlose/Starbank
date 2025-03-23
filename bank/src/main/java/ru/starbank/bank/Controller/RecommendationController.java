package ru.starbank.bank.Controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.RecommendationService;

import java.util.Collections;
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
    public List<Recommendation> getRecommendation(@PathVariable UUID user_id) {
        return recommendationService.getRecommendationsWithLogging(user_id);
    }


}