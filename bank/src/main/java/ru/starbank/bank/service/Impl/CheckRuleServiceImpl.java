package ru.starbank.bank.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.model.enums.QueryEnum;
import ru.starbank.bank.model.enums.TypeOfProductEnum;
import ru.starbank.bank.service.CheckRuleService;

import java.util.List;

@Service
public class CheckRuleServiceImpl implements CheckRuleService {

    @Override
    public void checkRule(Rule rule) {
        List<String> comparisons = List.of("<", ">", "<=", ">=", "==");
        if (rule.getQuery() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid field");
        }
        if (rule.getArguments().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arguments are empty");
        }
        boolean errorType = false;
        try {
            QueryEnum.valueOf(rule.getQuery());
            TypeOfProductEnum.valueOf(rule.getArguments().get(0));
        } catch (IllegalArgumentException r) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid type");
        }
        if (rule.getArguments().size() == 2 && !comparisons.contains(rule.getArguments().get(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid comparison");
        }
        if (rule.getArguments().size() >= 4 && rule.getArguments().size() <= 5) {
            if ((!rule.getArguments().get(1).equals("WITHDRAW") && !rule.getArguments().get(1).equals("DEPOSIT"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid type transaction");

            }
            if (!comparisons.contains(rule.getArguments().get(2))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid comparison");
            }
            if (!StringUtils.isNumeric(rule.getArguments().get(3))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid number");
            }
            if (rule.getArguments().size() == 5) {
                if (!rule.getArguments().get(4).equals("OR")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid OR");
                }
            }
        }
        if (rule.getArguments().size() == 3 || rule.getArguments().size() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "more Arguments");
        }
    }
}
