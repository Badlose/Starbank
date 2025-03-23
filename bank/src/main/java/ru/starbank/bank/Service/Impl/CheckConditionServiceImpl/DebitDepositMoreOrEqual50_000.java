package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

@Qualifier("DebitDepositMoreOrEqual50_000")
@Component
public class DebitDepositMoreOrEqual50_000
        implements CheckConditionService {

    private final JdbcTemplate jdbcTemplate;

    private final RecommendationsRepository recommendationsRepository;

    public DebitDepositMoreOrEqual50_000(JdbcTemplate jdbcTemplate, RecommendationsRepository recommendationsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {

        int MIN_DEBIT_AMOUNT = 50000;

        Integer totalAmount = jdbcTemplate.queryForObject(
                recommendationsRepository.DebitDeposit(userId),
                Integer.class,
                userId
        );

        return (totalAmount != null) && (totalAmount >= MIN_DEBIT_AMOUNT);
    }
}
