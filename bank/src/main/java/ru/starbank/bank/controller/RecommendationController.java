package ru.starbank.bank.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.dto.ListDynamicRecommendationDTO;
import ru.starbank.bank.dto.StatisticsDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.service.RecommendationService;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    public final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }


    @GetMapping("/userId/{userId}")
    public UserRecommendationsDTO getRecommendation(
            @Parameter(description = "Идентификатор поля (UUID)",
                    schema = @Schema(type = "string", format = "UUID",
                            example = "cd515076-5d8a-44be-930e-8d4fcb79f42d"))
            @PathVariable UUID userId) {
        return recommendationService.getRecommendation(userId);
    }


    @PostMapping("/rule/{recommendation}")
    public DynamicRecommendationDTO postNewDynamicRecommendation(@RequestBody DynamicRecommendation recommendation) {
        return recommendationService.createNewDynamicRecommendation(recommendation);
    }

    @GetMapping("/rule")
    public ListDynamicRecommendationDTO getAllDynamicRecommendations() {
        return recommendationService.getAllDynamicRecommendations();
    }

    @DeleteMapping("/rule/{recommendationId}")
    public ResponseEntity<Void> deleteDynamicRecommendation(@PathVariable Long recommendationId) {
        return ResponseEntity.status(recommendationService.deleteDynamicRecommendation(recommendationId)).build();
    }

    @GetMapping("/rule/stats")
    public StatisticsDTO getStatistics() {
        return recommendationService.getStatistics();
    }

}