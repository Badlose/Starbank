package ru.starbank.bank.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.model.enums.QueryEnum;
import ru.starbank.bank.model.enums.TypeOfProductEnum;
import ru.starbank.bank.service.RecommendationCorrect;
import ru.starbank.bank.service.RuleCorrect;

import java.util.List;

@Service
public class RecommendationCorrectImpl implements RecommendationCorrect {

    private RuleCorrect ruleCorrect;
    public boolean checkRecommendationCorrect(DynamicRecommendation recommendation){
        if(recommendation == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid field");
        }if (recommendation.getName()==null || recommendation.getProductId()==null || recommendation.getText()==null || recommendation.getRuleList()==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid field");
        } else {
            if(recommendation.getRuleList().isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"RuleList is empty");
            }
            for(Rule rule: recommendation.getRuleList()){
                ruleCorrect.checkRule(rule);
            }
        }
        return true;
    }
}
