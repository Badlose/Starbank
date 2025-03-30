package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.dto.ListDynamicRecommendationDTO;
import ru.starbank.bank.dto.RuleDTO;
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
import java.util.stream.Stream;

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
    @Cacheable(value = "recommendationCache", key = "#userId")
    public UserRecommendationsDTO getRecommendation(UUID userId) {

        List<DynamicRecommendation> recommendations = recommendationsRepository.findAll();

        List<DynamicRecommendationDTO> recommendationListForDto = new ArrayList<>();

        for (DynamicRecommendation recommendation : recommendations) {

            boolean resultCheck = checkerService.checkDynamicRecommendation(userId, recommendation);

            if (resultCheck) {
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
                        recommendation.getProduct_id(),
                        recommendation.getText(),
                        ruleDtoList
                );

                recommendationListForDto.add(recommendationDTO);
            }
        }

        return new UserRecommendationsDTO(userId, recommendationListForDto);
    }

    @Override
    @CachePut(value = "recommendationCache", key = "#recommendation.id")
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
                    recommendation.getProduct_id(),
                    recommendation.getText(),
                    ruleDtoList
            );

        return recommendationDTO;
    }

    @Override
    @Cacheable(value = "recommendationCache", key = "'allDynamicRecommendations'")
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
                    recommendation.getProduct_id(),
                    recommendation.getText(),
                    ruleDtoList
            );
            data.add(recommendationDTO);
        }

        ListDynamicRecommendationDTO dynamicRecommendationDTOList = new ListDynamicRecommendationDTO(data);
        return dynamicRecommendationDTOList;
    }

    @Override
    @CacheEvict(value = "recommendationCache", key = "#id")
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

}
