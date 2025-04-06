package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.exceptions.IncorrectRuleQueryException;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.service.RecommendationCheckerService;
import ru.starbank.bank.service.RecommendationRuleService;
import ru.starbank.bank.service.RuleCorrect;

import java.util.List;
import java.util.UUID;

@Component
public class RecommendationCheckerServiceImpl implements RecommendationCheckerService {

    private final List<RecommendationRuleService> ruleSets;
    private final RuleCorrect ruleService;


    public RecommendationCheckerServiceImpl(@Qualifier("USER_OF") RecommendationRuleService ruleUserOf,
                                            @Qualifier("ACTIVE_USER_OF") RecommendationRuleService ruleActiveUserOf,
                                            @Qualifier("TRANSACTION_SUM_COMPARE")
                                            RecommendationRuleService ruleTransactionSumCompare,
                                            @Qualifier("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
                                            RecommendationRuleService ruleTransactionSumCompareDepositWithdraw, RuleCorrect ruleService) {
        this.ruleService = ruleService;
        this.ruleSets = List.of(
                ruleUserOf,
                ruleActiveUserOf,
                ruleTransactionSumCompare,
                ruleTransactionSumCompareDepositWithdraw
        );
    }

    @Override
    public boolean checkDynamicRecommendation(UUID userId, DynamicRecommendation recommendation) {

        boolean check = true;
        boolean checkOr = false;
        boolean checkIf = true;

        for (Rule rule : recommendation.getRuleList()) {
            ruleService.checkRule(rule);
            switch (rule.getQuery()) {
                case "USER_OF" -> check = check && ruleSets.get(0).check(userId, rule);
                case "ACTIVE_USER_OF" -> check = check && ruleSets.get(1).check(userId, rule);
                case "TRANSACTION_SUM_COMPARE" -> {
                    if (rule.getArguments().contains("OR")) {
                        checkOr = checkOr || ruleSets.get(2).check(userId, rule);
                        checkIf = false;
                    } else {
                        check = check && ruleSets.get(2).check(userId, rule);
                    }
                }
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> check = check && ruleSets.get(3).check(userId, rule);
                default -> throw new IncorrectRuleQueryException("Incorrect query.");
            }

        }
        return check && (checkOr || checkIf);
    }
}

//        if (ruleSets != null) {
//            checkResult = checkResult || ruleSet.check(userId, rule);
//        } else {
//            throw new NullPointerException("Ruleset is null exception");
//        }
//
//        boolean resultCheck = true;
//        boolean check = false;
//        boolean checkIf = true;
//
//        for (Rule rule : recommendation.getRuleList()) {
//
//            if (rule.getArguments().contains("OR")) {
//
//                check = check || ruleSets.stream()
//                        .allMatch(r -> r.check(userId, rule));
//                checkIf = false;
//
//            } else {
//
//                resultCheck = resultCheck && ruleSets.stream()
//                        .allMatch(r -> r.check(userId, rule));
//            }
//        }
//
//        return resultCheck && (check || checkIf);