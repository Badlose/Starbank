package ru.starbank.bank.service.Impl;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
import ru.starbank.bank.dto.*;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.repository.RulesRepository;
import ru.starbank.bank.service.RecommendationCheckerService;
import ru.starbank.bank.service.RecommendationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Transactional
    public UserRecommendationsDTO getRecommendation(UUID userId) {

        List<DynamicRecommendation> recommendations = recommendationsRepository.findAll();

        List<UserDTO> recommendationListForDto = new ArrayList<>();

        for (DynamicRecommendation recommendation : recommendations) {

            boolean resultCheck = checkerService.checkDynamicRecommendation(userId, recommendation);

            if (resultCheck) {
                UserDTO recommendationDTO = new UserDTO(
                        recommendation.getName(),
                        recommendation.getProductId(),
                        recommendation.getText()
                );

                recommendationListForDto.add(recommendationDTO);
            }
        }

        return new UserRecommendationsDTO(userId, recommendationListForDto);
    }

    @Override
    @Transactional
    public DynamicRecommendationDTO createNewDynamicRecommendation(DynamicRecommendation recommendation) {
        recommendationsRepository.save(recommendation);
        for (Rule rule : recommendation.getRuleList()) {
            rule.setDynamicRecommendation(recommendation);
            rulesRepository.save(rule);
        }

        List<RuleDTO> ruleDtoList = recommendation.getRuleList().stream()
                .map(rule -> new RuleDTO(
                        rule.getQuery(),
                        rule.getArguments(),
                        rule.isNegate()
                ))
                .toList();

        DynamicRecommendationDTO recommendationDTO = new DynamicRecommendationDTO(
                recommendation.getId(),
                recommendation.getName(),
                recommendation.getProductId(),
                recommendation.getText(),
                ruleDtoList
        );

        return recommendationDTO;
    }

    @Override
    @Transactional
    public ListDynamicRecommendationDTO getAllDynamicRecommendations() {
        List<DynamicRecommendation> recommendations = recommendationsRepository.findAll();

        List<DynamicRecommendationDTO> data = new ArrayList<>();

        for (DynamicRecommendation recommendation : recommendations) {
            List<RuleDTO> ruleDtoList = recommendation.getRuleList().stream()
                    .map(rule -> new RuleDTO(
                            rule.getQuery(),
                            rule.getArguments(),
                            rule.isNegate()
                    ))
                    .toList();

            DynamicRecommendationDTO recommendationDTO = new DynamicRecommendationDTO(
                    recommendation.getId(),
                    recommendation.getName(),
                    recommendation.getProductId(),
                    recommendation.getText(),
                    ruleDtoList
            );
            data.add(recommendationDTO);
        }

        ListDynamicRecommendationDTO dynamicRecommendationDTOList = new ListDynamicRecommendationDTO(data);
        return dynamicRecommendationDTOList;
    }

    @Override
    @Transactional
    public void deleteDynamicRecommendation(Long id) {
        DynamicRecommendation recommendation = recommendationsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        for (Rule e : recommendation.getRuleList()) {
            rulesRepository.deleteById(e.getId());
        }
        recommendationsRepository.deleteById(id);
    }

    @Override
    public StatisticsDTO getStatistics() {

        return null;
    }

}
