package ru.starbank.bank.Controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }



    @GetMapping("/user_id/{userId}")
    public Optional<List<Recommendation>> getRecommendation(
            @Parameter(description = "Идентификатор пользователя (UUID)",
                    schema = @Schema(type = "string", format = "uuid",
                            example = "cd515076-5d8a-44be-930e-8d4fcb79f42d"))
            @PathVariable UUID userId) {
        return recommendationService.getRecommendation(userId);
    }




//    @GetMapping("/amount/{userId}")
//    public Integer getAmountTest(
//            @Parameter(description = "Идентификатор пользователя (UUID)",
//                    schema = @Schema(type = "string", format = "uuid",
//                            example = "a1b2c3d4-e5f6-4789-9abc-def012345678"))
//            @PathVariable UUID userId) {
//        return recommendationService.getAmount(userId);
//    }
}
