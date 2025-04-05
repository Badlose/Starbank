package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.service.RecommendationCheckerService;
import ru.starbank.bank.service.RecommendationRuleSet;

import java.util.List;
import java.util.UUID;

@Component
public class RecommendationCheckerServiceImpl implements RecommendationCheckerService {

    @Autowired
    private final List<RecommendationRuleSet> ruleSets;


    public RecommendationCheckerServiceImpl(@Qualifier("USER_OF") RecommendationRuleSet ruleSetUserOf,
                                            @Qualifier("TRANSACTION_SUM_COMPARE")
                                            RecommendationRuleSet ruleSetTransactionSumCompare,
                                            @Qualifier("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
                                            RecommendationRuleSet ruleSetTransactionSumCompareDepositWithdraw) {
        this.ruleSets = List.of(
                ruleSetUserOf,
                ruleSetTransactionSumCompare,
                ruleSetTransactionSumCompareDepositWithdraw
        );
    }

    @Override
    public boolean checkDynamicRecommendation(UUID userId, DynamicRecommendation recommendation) {

        boolean resultCheck = true;
        boolean check = false;
        boolean checkIf = true;

        for (Rule rule : recommendation.getRuleList()) {
            if(rule.getArguments().contains("OR")){
                check = check || ruleSets.stream()
                        .allMatch(r -> r.check(userId, rule));
                checkIf = false;
            }else {
                resultCheck = resultCheck && ruleSets.stream()
                        .allMatch(r -> r.check(userId, rule));
            }
        }

        return resultCheck && (check || checkIf);

    }
}
