package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

public class OrDebitOrSavingDepositMoreOrEqual50_000 implements CheckConditionService {

    DebitDepositMoreOrEqual50_000 debitDepositMoreOrEqual50000;
    SavingDepositMoreOrEqual50_000 savingDepositMoreOrEqual50000;

    @Override
    public boolean checkCondition(UUID userId) {
        return debitDepositMoreOrEqual50000.checkCondition(userId)
                || savingDepositMoreOrEqual50000.checkCondition(userId);
    }
}
