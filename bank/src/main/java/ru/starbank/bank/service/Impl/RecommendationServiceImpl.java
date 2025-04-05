package ru.starbank.bank.service.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.starbank.bank.dto.*;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.repository.RulesRepository;
import ru.starbank.bank.service.RecommendationCheckerService;
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
    @Transactional
    public UserRecommendationsDTO getRecommendation(UUID userId) {

        List<DynamicRecommendation> recommendations = recommendationsRepository.findAll();

        List<UserDTO> recommendationListForDto = new ArrayList<>();

        for (DynamicRecommendation recommendation : recommendations) {

            boolean resultCheck = checkerService.checkDynamicRecommendation(userId, recommendation);

            if (resultCheck) {
                recommendationListForDto.add(UserDTO.from(recommendation));
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
                .map(RuleDTO::from)
                .toList();
        return DynamicRecommendationDTO.from(recommendation,ruleDtoList);
    }

    @Override
    @Transactional
    public ListDynamicRecommendationDTO getAllDynamicRecommendations() {
        List<DynamicRecommendation> recommendations = recommendationsRepository.findAll();

        List<DynamicRecommendationDTO> data = new ArrayList<>();

        for (DynamicRecommendation recommendation : recommendations) {
            List<RuleDTO> ruleDtoList = recommendation.getRuleList().stream()
                    .map(RuleDTO::from)
                    .toList();

            data.add(DynamicRecommendationDTO.from(recommendation,ruleDtoList));
        }

        return new ListDynamicRecommendationDTO(data);
    }

    @Override
    @Transactional
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

    @Override
    public StatisticsDTO getStatistics() {

        return null;
    }

}
