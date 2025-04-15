package ru.starbank.bank.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.service.CheckRecommendationService;
import ru.starbank.bank.service.CheckRuleService;

@Service
public class CheckRecommendationServiceImpl implements CheckRecommendationService {
    @Autowired
    private CheckRuleService checkRuleService;

    public boolean checkRecommendationCorrect(DynamicRecommendation recommendation) {
        if (recommendation == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid field");
        }
        if (recommendation.getName() == null || recommendation.getProductId() == null || recommendation.getText() == null || recommendation.getRuleList() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid field");
        } else {
            if (recommendation.getRuleList().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RuleList is empty");
            }
            for (Rule rule : recommendation.getRuleList()) {
                checkRuleService.checkRule(rule);
            }
        }
        return true;
    }

}
