package ru.starbank.bank.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.starbank.bank.exceptions.*;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.model.enums.QueryEnum;
import ru.starbank.bank.model.enums.TypeOfProductEnum;
import ru.starbank.bank.service.CheckRuleService;

import java.util.List;

@Service
public class CheckRuleServiceImpl implements CheckRuleService {

    @Override
    public void checkRule(Rule rule) {
        List<String> arguments = rule.getArguments();
        int argumentsSize = arguments.size();
        List<String> comparisons = List.of("<", ">", "<=", ">=", "==");
        //boolean errorType = false;

        if (rule.getQuery() == null) {
            throw new QueryNullPointerException("Query is null");
        }
        if (arguments.isEmpty()) {
            throw new EmptyRuleArgumentsException("Arguments are empty");
        }
        try {
            QueryEnum.valueOf(rule.getQuery());
        } catch (IllegalArgumentException r) {
            throw new IncorrectRuleQueryValueException("Incorrect rule query");
        }
        try {
            TypeOfProductEnum.valueOf(arguments.get(0));
        } catch (IllegalArgumentException r) {
            throw new IncorrectProductTypeException("Incorrect product type");
        }
        if (argumentsSize == 2 && !comparisons.contains(arguments.get(1))) {
            throw new IncorrectComparisonSymbolException("Incorrect comparison symbol. Somebody trying to drop your database :(");
        }
        if (argumentsSize >= 4 && argumentsSize <= 5) {
            if ((!arguments.get(1).equals("WITHDRAW") && !arguments.get(1).equals("DEPOSIT"))) {
                throw new IncorrectTransactionTypeException("Incorrect transaction type");
            }
            if (!comparisons.contains(arguments.get(2))) {
                throw new IncorrectComparisonSymbolException("Incorrect comparison symbol. Somebody trying to drop your database :(");
            }
            if (!StringUtils.isNumeric(arguments.get(3))) {
                throw new IncorrectNumberForComparisonException("Incorrect number for comparison");
            }
            if (argumentsSize == 5) {
                if (!arguments.get(4).equals("OR")) {
                    throw new IncorrectRuleORArgumentException("Incorrect OR argument");
                }
            }
        }
        if (argumentsSize == 3 || argumentsSize > 5) {
            throw new ArgumentsSizeException("Incorrect arguments size");
        }
    }

}
