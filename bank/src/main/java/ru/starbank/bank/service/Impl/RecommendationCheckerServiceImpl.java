package ru.starbank.bank.service.Impl;

import org.springframework.stereotype.Component;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.service.RecommendationCheckerService;
import ru.starbank.bank.service.RecommendationRuleSetService;

import java.util.Map;
import java.util.UUID;

@Component
public class RecommendationCheckerServiceImpl implements RecommendationCheckerService {

    private final Map<String, RecommendationRuleSetService> ruleSets;

    public RecommendationCheckerServiceImpl(Map<String, RecommendationRuleSetService> ruleSets) {
        this.ruleSets = ruleSets;
    }

    @Override
    public boolean checkDynamicRecommendation(UUID userId, DynamicRecommendation recommendation) {

        boolean checkResult = false;

        for (Rule rule : recommendation.getRuleList()) {

            RecommendationRuleSetService ruleSet = ruleSets.get(rule.getQuery());

            checkResult = checkResult || ruleSet.check(userId, rule);

        }

        return checkResult;
    }

//
//    boolean resultCheck = true;
//    boolean check = false;
//    boolean checkIf = true;
//
//        for(
//    Rule rule :recommendation.getRuleList())
//
//    {
//        if (rule.getArguments().contains("OR")) {
//            check = check || ruleSets.stream()
//                    .allMatch(r -> r.check(userId, rule));
//            checkIf = false;
//        } else {
//            resultCheck = resultCheck && ruleSets.stream()
//                    .allMatch(r -> r.check(userId, rule));
//        }
//    }
//
//        return resultCheck &&(check ||checkIf);

//                if (ruleSet != null) {
//        checkResult = checkResult || ruleSet.check(userId, rule);
//    } else {
//        throw new NullPointerException("Ruleset is null exception");
//    }
}
