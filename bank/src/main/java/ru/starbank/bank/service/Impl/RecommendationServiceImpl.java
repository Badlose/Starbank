package ru.starbank.bank.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ResponseStatusException;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.dto.ListDynamicRecommendationDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.dto.mapper.DynamicRecommendationMapper;
import ru.starbank.bank.dto.mapper.ListDynamicRecommendationMapper;
import ru.starbank.bank.dto.mapper.UserRecommendationMapper;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.model.Statistic;
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
    private final DynamicRecommendationMapper recommendationMapper;
    private final UserRecommendationMapper userRecommendationMapper;
    private final ListDynamicRecommendationMapper listDynamicRecommendationMapper;
    private final CheckCorrect checkCorrect;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);


    public RecommendationServiceImpl(RecommendationsRepository recommendationsRepository,
                                     RulesRepository rulesRepository,
                                     StatisticRepository statisticRepository,
                                     RecommendationCheckerService checkerService, DynamicRecommendationMapper recommendationMapper, UserRecommendationMapper userRecommendationMapper, ListDynamicRecommendationMapper listDynamicRecommendationMapper, CheckCorrect checkCorrect) {
        this.recommendationsRepository = recommendationsRepository;
        this.rulesRepository = rulesRepository;
        this.statisticRepository = statisticRepository;
        this.checkerService = checkerService;
        this.recommendationMapper = recommendationMapper;
        this.userRecommendationMapper = userRecommendationMapper;
        this.listDynamicRecommendationMapper = listDynamicRecommendationMapper;
        this.checkCorrect = checkCorrect;
    }

    @Override
    @Transactional
    public UserRecommendationsDTO getRecommendation(UUID userId) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<DynamicRecommendation> recommendations = recommendationsRepository.findAll();
        List<DynamicRecommendation> recommendationListForDto = new ArrayList<>();

        for (DynamicRecommendation recommendation : recommendations) {
            boolean resultCheck = checkerService.checkDynamicRecommendation(userId, recommendation);
            if (resultCheck) {
                Statistic statistic = statisticRepository.findByRecommendationId(recommendation.getId());
                statistic.setCounter(statistic.getCounter() + 1);
                recommendationListForDto.add(recommendation);
            }
        }
        stopWatch.stop();
        long executionTime = stopWatch.getTotalTimeNanos();
        logger.info("ЛОГГЕР ИЗ ГЕТ РЕКОММЕНДЕЙШН {} ms for user {}.",
                executionTime, userId);
        return userRecommendationMapper.toRecommendationResponseDto(
                userId, recommendationListForDto);
    }

    @Override
    @Transactional
    public DynamicRecommendationDTO createNewDynamicRecommendation(DynamicRecommendation recommendation) {
        if (checkCorrect.checkRecommendationCorrect(recommendation)) {
            recommendationsRepository.save(recommendation);

            Statistic statistic = new Statistic(recommendation.getId(), 0);
            statisticRepository.save(statistic);

            for (Rule rule : recommendation.getRuleList()) {
                rule.setDynamicRecommendation(recommendation);
                rulesRepository.save(rule);
            }
            return recommendationMapper.toDynamicRecommendationDto(recommendation);
        } else throw new RuntimeException("rec invalid");
    }

    @Override
    @Transactional
    public ListDynamicRecommendationDTO getAllDynamicRecommendations() {
        return listDynamicRecommendationMapper.toRecommendationListResponseDto(recommendationsRepository.findAll());
    }

    @Override
    @Transactional
    public void deleteDynamicRecommendation(Long id) {
        DynamicRecommendation recommendation = recommendationsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        statisticRepository.deleteById(recommendation.getId());

        for (Rule e : recommendation.getRuleList()) { //запускать удаление рулов если удалили статы
            rulesRepository.deleteById(e.getId());
        }

        recommendationsRepository.deleteById(id); //запускать если удалили рулы
    }

    @Override
    public List<Statistic> getStatistics() {
        return statisticRepository.findAll();
    }

}
