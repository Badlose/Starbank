package ru.starbank.bank.service.Impl;

import org.springframework.stereotype.Service;
import ru.starbank.bank.exceptions.IncorrectRuleArgumentsException;
import ru.starbank.bank.exceptions.IncorrectRuleException;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.service.RuleService;

import java.util.List;

@Service
public class RuleServiceImpl implements RuleService {

    @Override
    public void checkRule(Rule rule) {

        if (rule == null) {
            throw new IncorrectRuleException();
        }

        List<String> arguments = rule.getArguments();
        if (arguments.isEmpty()) {
            throw new IncorrectRuleArgumentsException("List<Arguments> is empty");
        }
        String productType = arguments.get(0);

        if (productType.isEmpty()) {
            throw new IncorrectRuleArgumentsException("Product type is empty");
        }

        if (rule.getQuery().equals("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")) {
            checkComparisonSymbol(rule.getArguments().get(1));
        }

    }

    private void checkComparisonSymbol(String comparison) {
        if (!(comparison.equals("<") || comparison.equals("<=") || comparison.equals(">") || comparison.equals(">=") || comparison.equals("=="))) {
            throw new IllegalArgumentException("Illegal comparison operator: " + comparison);
        }
    }
}
