package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

@Qualifier("OrDebitOrSavingDepositMoreOrEqual50_000")
@Component
public class OrDebitOrSavingDepositMoreOrEqual50_000 implements CheckConditionService {

    private final DebitDepositMoreOrEqual50_000 debitDepositMoreOrEqual50000;
    private final SavingDepositMoreOrEqual50_000 savingDepositMoreOrEqual50000;

    public OrDebitOrSavingDepositMoreOrEqual50_000(
            @Qualifier("DebitDepositMoreOrEqual50_000") DebitDepositMoreOrEqual50_000 debitDepositMoreOrEqual50000,
            @Qualifier("SavingDepositMoreOrEqual50_000") SavingDepositMoreOrEqual50_000 savingDepositMoreOrEqual50000) {
        this.debitDepositMoreOrEqual50000 = debitDepositMoreOrEqual50000;
        this.savingDepositMoreOrEqual50000 = savingDepositMoreOrEqual50000;
    }

    @Override
    public boolean checkCondition(UUID userId) {
        return debitDepositMoreOrEqual50000.checkCondition(userId)
                || savingDepositMoreOrEqual50000.checkCondition(userId);
    }
}
