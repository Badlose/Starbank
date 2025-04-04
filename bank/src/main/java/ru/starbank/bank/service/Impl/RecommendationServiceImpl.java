package ru.starbank.bank.service.Impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.starbank.bank.dto.*;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.repository.RulesRepository;
import ru.starbank.bank.repository.StatisticRepository;
import ru.starbank.bank.service.RecommendationCheckerService;
import ru.starbank.bank.service.RecommendationService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationsRepository recommendationsRepository;

    private final RulesRepository rulesRepository;

    private final StatisticRepository statisticRepository;

    private final RecommendationCheckerService checkerService;

    public RecommendationServiceImpl(RecommendationsRepository recommendationsRepository, RulesRepository rulesRepository, StatisticRepository statisticRepository, RecommendationCheckerService checkerService) {
        this.recommendationsRepository = recommendationsRepository;
        this.rulesRepository = rulesRepository;
        this.statisticRepository = statisticRepository;
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

        Statistic statistic = new Statistic(recommendation.getId(), 0);
        statisticRepository.save(statistic);

        for (Rule rule : recommendation.getRuleList()) {
            rule.setDynamicRecommendation(recommendation);
            rulesRepository.save(rule);
        }

        List<RuleDTO> ruleDtoList = recommendation.getRuleList().stream()
                .map(RuleDTO::from)
                .toList();
        return DynamicRecommendationDTO.from(recommendation, ruleDtoList);
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

            data.add(DynamicRecommendationDTO.from(recommendation, ruleDtoList));
        }

        return new ListDynamicRecommendationDTO(data);
    }

    @Override
    @CacheEvict(value = "recommendationCache", key = "#id")
    @Transactional
    public void deleteDynamicRecommendation(Long id) {
        DynamicRecommendation recommendation = recommendationsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        statisticRepository.deleteById(recommendation.getId());

        for (Rule e : recommendation.getRuleList()) { //апускать удаление рулов если удалили статы
            rulesRepository.deleteById(e.getId());
        }

        recommendationsRepository.deleteById(id); //запускать если удалили рулы


    }

    @Override
    public List<Statistic> getStatistics() {
        List<Statistic> statistics = statisticRepository.findAll();
        return statistics;
    }

}
