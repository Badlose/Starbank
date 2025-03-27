package ru.starbank.bank.Controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.starbank.bank.Model.DynamicRecommendation;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.RecommendationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    public final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/userId/{userId}")
    public List<Recommendation> getRecommendation(
            @Parameter(description = "Идентификатор поля (UUID)",
                    schema = @Schema(type = "string", format = "UUID",
                            example = "cd515076-5d8a-44be-930e-8d4fcb79f42d"))
            @PathVariable UUID userId) {
        return recommendationService.getRecommendation(userId);
    }


    @PostMapping("/post/{recommendation}")
    public DynamicRecommendation postNewDynamicRecommendation(@RequestBody DynamicRecommendation recommendation) {
        return recommendationService.createNewDynamicRecommendation(recommendation);
    }

    @GetMapping("/get")
    public List<DynamicRecommendation> getAllDynamicRecommendations() {
        return recommendationService.getAllDynamicRecommendations();
    }

    @DeleteMapping("/delete/{recommendationId}")
    public ResponseEntity<ResponseStatus> deleteDynamicRecommendation(@PathVariable Long recommendationId) { //возможно не тот тип возвращаемого значения
        recommendationService.deleteDynamicRecommendation(recommendationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}