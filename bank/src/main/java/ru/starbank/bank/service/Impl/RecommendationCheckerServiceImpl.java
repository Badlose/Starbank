package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.repository.RulesRepository;
import ru.starbank.bank.service.RecommendationCheckerService;
import ru.starbank.bank.service.RecommendationRuleSet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class RecommendationCheckerServiceImpl implements RecommendationCheckerService {


    private final RecommendationsRepository recommendationsRepository;

    private final RulesRepository rulesRepository;

    @Autowired
    private final List<RecommendationRuleSet> ruleSets;


    public RecommendationCheckerServiceImpl(RecommendationsRepository recommendationsRepository,
                                            RulesRepository rulesRepository,
                                            @Qualifier("USER_OF") RecommendationRuleSet ruleSetUserOf,
                                            @Qualifier("TRANSACTION_SUM_COMPARE")
                                            RecommendationRuleSet ruleSetTransactionSumCompare,
                                            @Qualifier("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
                                            RecommendationRuleSet ruleSetTransactionSumCompareDepositWithdraw) {
        this.recommendationsRepository = recommendationsRepository;
        this.rulesRepository = rulesRepository;
        this.ruleSets = List.of(
                ruleSetUserOf,
                ruleSetTransactionSumCompare,
                ruleSetTransactionSumCompareDepositWithdraw
        );
    }

    @Override
    public boolean checkDynamicRecommendation(UUID userId, DynamicRecommendation recommendation) {

        boolean resultCheck = false;

        for (Rule rule : recommendation.getRuleList()) {
            resultCheck = ruleSets.stream()
                    .allMatch(r -> r.check(userId, rule));
        }

        return resultCheck;

    }
}
