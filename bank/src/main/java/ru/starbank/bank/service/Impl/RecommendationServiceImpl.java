package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.starbank.bank.dto.*;
import ru.starbank.bank.dto.mapper.DynamicRecommendationMapper;
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

    public RecommendationServiceImpl(RecommendationsRepository recommendationsRepository,
                                     RulesRepository rulesRepository,
                                     StatisticRepository statisticRepository,
                                     RecommendationCheckerService checkerService, DynamicRecommendationMapper recommendationMapper) {
        this.recommendationsRepository = recommendationsRepository;
        this.rulesRepository = rulesRepository;
        this.statisticRepository = statisticRepository;
        this.checkerService = checkerService;
        this.recommendationMapper = recommendationMapper;
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

                Statistic statistic = statisticRepository.findByRecommendationId(recommendation.getId());

                statistic.setCounter(statistic.getCounter() + 1);

                recommendationListForDto.add(recommendationDTO);

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

//        List<RuleDTO> ruleDtoList = recommendation.getRuleList().stream()
//                .map(rule -> new RuleDTO(
//                        rule.getQuery(),
//                        rule.getArguments(),
//                        rule.isNegate()
//                ))
//                .toList();

        DynamicRecommendationDTO recommendationDTO = recommendationMapper.convert(recommendation);
//        DynamicRecommendationDTO recommendationDTO = new DynamicRecommendationDTO(
//                recommendation.getId(),
//                recommendation.getName(),
//                recommendation.getProductId(),
//                recommendation.getText(),
//                ruleDtoList
//        );

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

            DynamicRecommendationDTO recommendationDTO = recommendationMapper.convert(recommendation);
//            DynamicRecommendationDTO recommendationDTO = new DynamicRecommendationDTO(
//                    recommendation.getId(),
//                    recommendation.getName(),
//                    recommendation.getProductId(),
//                    recommendation.getText(),
//                    ruleDtoList
//            );
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
