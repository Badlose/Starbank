package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.dto.ListDynamicRecommendationDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.repository.RulesRepository;
import ru.starbank.bank.service.RecommendationCheckerService;
import ru.starbank.bank.service.RecommendationRuleSet;
import ru.starbank.bank.service.RecommendationService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationsRepository recommendationsRepository;

    private final RulesRepository rulesRepository;

    private final RecommendationCheckerService checkerService;

    public RecommendationServiceImpl(RecommendationsRepository recommendationsRepository, RulesRepository rulesRepository, RecommendationCheckerService checkerService) {
        this.recommendationsRepository = recommendationsRepository;
        this.rulesRepository = rulesRepository;
        this.checkerService = checkerService;
    }

    @Override
    public UserRecommendationsDTO getRecommendation(UUID userId) {

        List<DynamicRecommendation> recommendations = recommendationsRepository.findAll();

        List<DynamicRecommendationDTO> recommendationListForDto = new ArrayList<>();

        for (DynamicRecommendation recommendation : recommendations) {

            boolean resultCheck = checkerService.checkDynamicRecommendation(userId, recommendation);

            if (resultCheck) {
                DynamicRecommendationDTO dto = new DynamicRecommendationDTO(
                        recommendation.getId(),
                        recommendation.getName(),
                        recommendation.getProduct_id(),
                        recommendation.getText(),
                        recommendation.getRuleList());

                recommendationListForDto.add(dto);
            }
        }

        return new UserRecommendationsDTO(userId, recommendationListForDto);
    }

    @Override
    public DynamicRecommendationDTO createNewDynamicRecommendation(DynamicRecommendation recommendation) {
        recommendationsRepository.save(recommendation);
        for (Rule rule : recommendation.getRuleList()) {
            rule.setDynamicRecommendation(recommendation);
            rulesRepository.save(rule);
        }
        return new DynamicRecommendationDTO(
                recommendation.getId(),
                recommendation.getName(),
                recommendation.getProduct_id(),
                recommendation.getText(),
                recommendation.getRuleList()
        );
    }

    @Override
    public ListDynamicRecommendationDTO getAllDynamicRecommendations() {
        List<DynamicRecommendation> recommendations = recommendationsRepository.findAll();
        List<DynamicRecommendationDTO> data = recommendations.stream()
                .map(recommendation -> new DynamicRecommendationDTO(
                        recommendation.getId(),
                        recommendation.getName(),
                        recommendation.getProduct_id(),
                        recommendation.getText(),
                        recommendation.getRuleList()))
                        .toList();
        return new ListDynamicRecommendationDTO(data);
    }

    @Override
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
