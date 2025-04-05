package ru.starbank.bank.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.model.enums.QueryEnum;
import ru.starbank.bank.model.enums.TypeOfProductEnum;

import java.util.List;

@Service
public class CheckCorrect {
    public boolean checkRecommendationCorrect(DynamicRecommendation recommendation){
        List<String> comparisons = List.of("<",">","<=",">=","==");
        if(recommendation == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid field");
        }if (recommendation.getName()==null || recommendation.getProductId()==null || recommendation.getText()==null || recommendation.getRuleList()==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid field");
        } else {
            for(Rule rule: recommendation.getRuleList()){
                if(rule.getArguments()==null || rule.getQuery()==null){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid field");
                }if(rule.getArguments().isEmpty()){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid field");
                }
                boolean errorType = false;
                try {
                    QueryEnum.valueOf(rule.getQuery());
                    TypeOfProductEnum.valueOf(rule.getArguments().get(0));
                } catch (IllegalArgumentException r) {
                    errorType = true;
                }
                if(errorType){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid type");
                }
                if(rule.getArguments().size()==2 && (!comparisons.contains(rule.getArguments().get(1)) || rule.getArguments().get(1).length()>2)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid comparison");
                }
                if(rule.getArguments().size()==4 && ((!rule.getArguments().get(1).equals("WITHDRAW") && !rule.getArguments().get(1).equals("DEPOSIT")) || (!comparisons.contains(rule.getArguments().get(2)) || rule.getArguments().get(2).length()>2) || !StringUtils.isNumeric(rule.getArguments().get(3)))){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid comparison or type transaction or number");
                }
                if(rule.getArguments().size()==5 && ((!rule.getArguments().get(1).equals("WITHDRAW") && !rule.getArguments().get(1).equals("DEPOSIT")) || (!comparisons.contains(rule.getArguments().get(2)) || rule.getArguments().get(2).length()>2) || !StringUtils.isNumeric(rule.getArguments().get(3)) || !rule.getArguments().get(3).equals("OR"))){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid comparison or type transaction or number or OR");
                }
            }
        }
        return true;
    }
}
