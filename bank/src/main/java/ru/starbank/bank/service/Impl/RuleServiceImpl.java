package ru.starbank.bank.service.Impl;

import ru.starbank.bank.exceptions.IncorrectRuleArgumentsException;
import ru.starbank.bank.exceptions.IncorrectRuleException;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.service.RuleService;

import java.util.List;

public class RuleServiceImpl implements RuleService {

    @Override
    public boolean checkRule(Rule rule) {

        if (rule == null) {
            throw new IncorrectRuleException();
        }

        List<String> arguments = rule.getArguments();
        String productType = arguments.get(0);
        String comparison = arguments.get(1);

        if (productType.isEmpty() || comparison.isEmpty()) {
            throw new IncorrectRuleArgumentsException("Product type or Comparison  symbol are empty");
        }

        if (rule.getQuery().equals("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")) {
            checkComparisonSymbol(rule.getArguments().get(1));
        }

        return true;
    }

    private void checkComparisonSymbol(String comparison) {
        if (!(comparison.equals("<") || comparison.equals("<=") || comparison.equals(">") || comparison.equals(">=") || comparison.equals("=="))) {
            throw new IllegalArgumentException("Illegal comparison operator: " + comparison);
        }
    }
}
