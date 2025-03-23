package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

@Qualifier("InvestNotUsing")
@Component
public class InvestNotUsing implements CheckConditionService {

    private final JdbcTemplate jdbcTemplate;

    private final RecommendationsRepository recommendationsRepository;

    public InvestNotUsing(JdbcTemplate jdbcTemplate, RecommendationsRepository recommendationsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {
        int result = jdbcTemplate.queryForObject(
                recommendationsRepository.InvestNotUsing(userId),
                int.class,
                userId
        );
        return result == 0;
    }
}
