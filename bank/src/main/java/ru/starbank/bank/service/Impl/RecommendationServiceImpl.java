package ru.starbank.bank.service.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.starbank.bank.dto.*;
import ru.starbank.bank.dto.mapper.DynamicRecommendationMapper;
import ru.starbank.bank.dto.mapper.ListDynamicRecommendationMapper;
import ru.starbank.bank.dto.mapper.UserRecommendationMapper;
import ru.starbank.bank.exceptions.SqlRequestException;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.model.Statistic;
import ru.starbank.bank.model.enums.QueryEnum;
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
    private final DynamicRecommendationMapper recommendationMapper;
    private final UserRecommendationMapper userRecommendationMapper;
    private final ListDynamicRecommendationMapper listDynamicRecommendationMapper;
    private final CheckCorrect checkCorrect;

    public RecommendationServiceImpl(RecommendationsRepository recommendationsRepository,
                                     RulesRepository rulesRepository,
                                     StatisticRepository statisticRepository,
                                     RecommendationCheckerService checkerService, DynamicRecommendationMapper recommendationMapper, UserRecommendationMapper userRecommendationMapper, ListDynamicRecommendationMapper listDynamicRecommendationMapper, CheckCorrect checkCorrect) {

        this.recommendationsRepository = recommendationsRepository;
        this.rulesRepository = rulesRepository;
        this.checkerService = checkerService;

        this.recommendationMapper = recommendationMapper;
        this.userRecommendationMapper = userRecommendationMapper;
        this.listDynamicRecommendationMapper = listDynamicRecommendationMapper;
        this.checkCorrect = checkCorrect;
    }

    @Override
    @Transactional
    public UserRecommendationsDTO getRecommendation(UUID userId) {
        List<DynamicRecommendation> recommendations = recommendationsRepository.findAll();
        List<UserDTO> recommendationListForDto = new ArrayList<>();

        for (DynamicRecommendation recommendation : recommendations) {
            boolean resultCheck = checkerService.checkDynamicRecommendation(userId, recommendation);
            if (resultCheck) {
                Statistic statistic = statisticRepository.findByRecommendationId(recommendation.getId());
                statistic.setCounter(statistic.getCounter() + 1);
                recommendationListForDto.add(recommendation);
            }
        }

        return new UserRecommendationsDTO(userId, recommendationListForDto);
    }

    @Override
    @Transactional
    public DynamicRecommendationDTO createNewDynamicRecommendation(DynamicRecommendation recommendation) {
        if(checkCorrect.checkRecommendationCorrect(recommendation)){
            recommendationsRepository.save(recommendation);

            Statistic statistic = new Statistic(recommendation.getId(), 0);
            statisticRepository.save(statistic);

            for (Rule rule : recommendation.getRuleList()) {
                rule.setDynamicRecommendation(recommendation);
                rulesRepository.save(rule);
            }
            return recommendationMapper.toDynamicRecommendationDto(recommendation);
        }
        return new DynamicRecommendationDTO();
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
