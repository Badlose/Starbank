package ru.starbank.bank.Service.Impl.CheckConditionServiceImpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.CheckConditionService;

import java.util.UUID;

@Qualifier("DebitDepositMoreThanWithdraw")
@Component
public class DebitDepositMoreThanWithdraw implements CheckConditionService {

    private final JdbcTemplate jdbcTemplate;

    private final RecommendationsRepository recommendationsRepository;

    public DebitDepositMoreThanWithdraw(JdbcTemplate jdbcTemplate, RecommendationsRepository recommendationsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public boolean checkCondition(UUID userId) {

        Integer totalDeposit = jdbcTemplate.queryForObject(
                recommendationsRepository.DebitDeposit(userId),
                Integer.class,
                userId
        );

        Integer totalWithdraw = jdbcTemplate.queryForObject(
                recommendationsRepository.DebitWithdraw(userId),
                Integer.class,
                userId
        );

        return (totalDeposit != null && totalWithdraw != null) && (totalDeposit > totalWithdraw);
    }
}
