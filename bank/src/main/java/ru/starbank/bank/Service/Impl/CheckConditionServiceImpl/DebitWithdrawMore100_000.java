package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

public class DebitWithdrawMore100_000
        implements CheckConditionService {

    private final JdbcTemplate jdbcTemplate;

    private final RecommendationsRepository recommendationsRepository;

    public DebitWithdrawMore100_000(JdbcTemplate jdbcTemplate, RecommendationsRepository recommendationsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {

        int MIN_DEBIT_WITHDRAW = 100000;

        Integer totalAmount = jdbcTemplate.queryForObject(
                recommendationsRepository.DebitWithdraw(userId),
                Integer.class,
                userId
        );

        return (totalAmount != null) && (totalAmount >= MIN_DEBIT_WITHDRAW);
    }
}
