package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

public class CreditNotUsing implements CheckConditionService {

    private final JdbcTemplate jdbcTemplate;

    private final RecommendationsRepository recommendationsRepository;

    public CreditNotUsing(JdbcTemplate jdbcTemplate, RecommendationsRepository recommendationsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {
        int result = jdbcTemplate.queryForObject(
                recommendationsRepository.CreditNotUsing(userId),
                int.class,
                userId
        );
        return result == 0;
    }
}
