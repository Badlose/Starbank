package ru.starbank.bank.Controller;

import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    public final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/user_id")
    public Optional<Recommendation> getRecommendation(@PathVariable UUID user_id) {
        return recommendationService.getRecommendation(user_id);
    }

}
