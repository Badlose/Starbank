package ru.starbank.bank.service;

import ru.starbank.bank.model.Rule;

public interface RuleService {

    boolean checkRule(Rule rule);
}
