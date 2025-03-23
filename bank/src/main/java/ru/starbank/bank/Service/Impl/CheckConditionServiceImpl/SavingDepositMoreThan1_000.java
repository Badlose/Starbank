package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

@Qualifier("SavingDepositMoreThan1_000")
@Component
public class SavingDepositMoreThan1_000 implements CheckConditionService {

    private final JdbcTemplate jdbcTemplate;

    private final RecommendationsRepository recommendationsRepository;

    public SavingDepositMoreThan1_000(JdbcTemplate jdbcTemplate, RecommendationsRepository recommendationsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {
        int MIN_SAVING_AMOUNT = 1000;

        Integer totalAmount = jdbcTemplate.queryForObject(
                recommendationsRepository.SavingDeposit(userId),
                Integer.class,
                userId
        );

        return (totalAmount != null) && (totalAmount > MIN_SAVING_AMOUNT);
    }
}
