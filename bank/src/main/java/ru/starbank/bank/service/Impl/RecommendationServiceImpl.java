package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.dto.ListDynamicRecommendationDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.repository.RulesRepository;
import ru.starbank.bank.service.RecommendationRuleSet;
import ru.starbank.bank.service.RecommendationService;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationsRepository recommendationsRepository;

    private final RulesRepository rulesRepository;

    @Autowired
    private final List<RecommendationRuleSet> ruleSets;


    public RecommendationServiceImpl(RecommendationsRepository recommendationsRepository,
                                     RulesRepository rulesRepository,
                                     @Qualifier("USER_OF") RecommendationRuleSet ruleSetUserOf,
                                     @Qualifier("ACTIVE_USER_OF") RecommendationRuleSet ruleSetActiveUserOf,
                                     @Qualifier("TRANSACTION_SUM_COMPARE")
                                     RecommendationRuleSet ruleSetTransactionSumCompare,
                                     @Qualifier("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
                                     RecommendationRuleSet ruleSetTransactionSumCompareDepositWithdraw) {
        this.recommendationsRepository = recommendationsRepository;
        this.rulesRepository = rulesRepository;
        this.ruleSets = List.of(
                ruleSetUserOf,
                ruleSetActiveUserOf,
                ruleSetTransactionSumCompare,
                ruleSetTransactionSumCompareDepositWithdraw
        );
    }

    @Override
    @Cacheable(value = "recommendationCache", key = "#userId")
    public UserRecommendationsDTO getRecommendation(UUID userId) {

        // тут вызов метода проверки и далее сборка ДТО

//        List<DynamicRecommendation> recommendations = ruleSets.stream()
//                .map(r -> r.check(userId))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .toList();

        return new UserRecommendationsDTO(); //дописать сборку ДТО
    }

    @Override
    @CachePut(value = "recommendationCache", key = "#recommendation.id")
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
    @Cacheable(value = "recommendationCache", key = "'allDynamicRecommendations'")
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
    @CacheEvict(value = "recommendationCache", key = "#id")
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
