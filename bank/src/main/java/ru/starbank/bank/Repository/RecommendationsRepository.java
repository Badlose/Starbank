package ru.starbank.bank.Repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean UsingDebitOriginal(UUID userId) {

        String query = """
                    SELECT COUNT(*) FROM newTable WHERE USER_ID = ? AND PRODUCT_TYPE = 'DEBIT'
                """;

        int result = jdbcTemplate.queryForObject(
                query,
                int.class,
                userId);
        return result > 0;
    }

    public boolean UsingDebit(UUID userId) {

        String query = """
                    SELECT COUNT(*) 
                    FROM TRANSACTIONS t 
                    INNER JOIN PRODUCTS p ON t.product_id = p.id 
                    WHERE t.USER_ID = ? AND p.TYPE = 'DEBIT'
                """;

        int result = jdbcTemplate.queryForObject(
                query,
                int.class,
                userId);
        return result > 0;
    }

    public boolean NotUsingInvestOriginal(UUID userId) {
        String query = """
                SELECT COUNT(*)
                FROM TRANSACTIONS
                WHERE USER_ID = ?
                  AND PRODUCT_TYPE = 'INVEST'
                """;

        int result = jdbcTemplate.queryForObject(
                query,
                int.class,
                userId
        );
        return result == 0;
    }

    public boolean NotUsingInvest(UUID userId) {
        String query = """
                SELECT COUNT(*)
                FROM TRANSACTIONS t 
                INNER JOIN PRODUCTS p ON t.product_id = p.id 
                WHERE t.USER_ID = ?
                  AND p.TYPE = 'INVEST'
                """;

        int result = jdbcTemplate.queryForObject(
                query,
                int.class,
                userId
        );
        return result == 0;
    }

    public boolean NotUsingCredit(UUID userId) {
        String query = """
                SELECT COUNT(*)
                FROM newTable
                WHERE USER_ID = ?
                  AND PRODUCT_TYPE = 'CREDIT'
                """;

        int result = jdbcTemplate.queryForObject(
                query,
                int.class,
                userId
        );
        return result == 0;
    }

    public boolean TotalDepositSavingMoreThan1_000_Original(UUID userId) {

        int MIN_SAVING_AMOUNT = 1000;

        String query = """
                SELECT SUM(AMOUNT)
                FROM newTable
                WHERE USER_ID = ?
                  AND PRODUCT_TYPE = 'SAVING'
                  AND TYPE = 'DEPOSIT'
                """;

        Integer totalAmount = jdbcTemplate.queryForObject(
                query,
                Integer.class,
                userId
        );

        return (totalAmount != null) && (totalAmount > MIN_SAVING_AMOUNT);
    }

    public boolean TotalDepositSavingMoreThan1_000(UUID userId) {

        int MIN_SAVING_AMOUNT = 1000;

        String query = """
                SELECT SUM(AMOUNT)
                FROM TRANSACTIONS t
                INNER JOIN PRODUCTS p ON t.product_id = p.id 
                WHERE t.USER_ID = ?
                  AND p.TYPE = 'SAVING'
                  AND t.TYPE = 'DEPOSIT'
                """;

        Integer totalAmount = jdbcTemplate.queryForObject(
                query,
                Integer.class,
                userId
        );

        return (totalAmount != null) && (totalAmount > MIN_SAVING_AMOUNT);
    }

    public boolean TotalDepositSavingMoreOrEqual50_000(UUID userId) {

        int MIN_SAVING_AMOUNT = 50000;

        String query = """
                SELECT SUM(AMOUNT)
                FROM newTable
                WHERE USER_ID = ?
                  AND PRODUCT_TYPE = 'SAVING'
                  AND TYPE = 'DEPOSIT'
                """;

        Integer totalAmount = jdbcTemplate.queryForObject(
                query,
                Integer.class,
                userId
        );

        return (totalAmount != null) && (totalAmount >= MIN_SAVING_AMOUNT);
    }

    public boolean TotalDepositDebitMoreThanTotalWithdrawDebit(UUID userId) {

        String queryDebit = """
                SELECT SUM(AMOUNT)
                FROM newTable
                WHERE USER_ID = ?
                  AND PRODUCT_TYPE = 'SAVING'
                  AND TYPE = 'DEPOSIT'
                """;

        String queryWithdraw = """
                SELECT SUM(AMOUNT)
                FROM newTable
                WHERE USER_ID = ?
                  AND PRODUCT_TYPE = 'SAVING'
                  AND TYPE = 'WITHDRAW'
                """;

        Integer totalDeposit = jdbcTemplate.queryForObject(
                queryDebit,
                Integer.class,
                userId
        );

        Integer totalWithdraw = jdbcTemplate.queryForObject(
                queryWithdraw,
                Integer.class,
                userId
        );

        return (totalDeposit != null) && (totalDeposit > totalWithdraw);
    }

    public boolean TotalDepositDebitMoreOrEqual50_000(UUID userId) {

        int MIN_DEBIT_AMOUNT = 50000;

        String query = """
                SELECT SUM(AMOUNT)
                FROM newTable
                WHERE USER_ID = ?
                  AND PRODUCT_TYPE = 'DEBIT'
                  AND TYPE = 'DEPOSIT'
                """;

        Integer totalAmount = jdbcTemplate.queryForObject(
                query,
                Integer.class,
                userId
        );

        return (totalAmount != null) && (totalAmount >= MIN_DEBIT_AMOUNT);
    }


    public int getRandomTransactionAmount(UUID user) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT AMOUNT FROM transactions t WHERE t.USER_ID = ? LIMIT 1",
                Integer.class,
                user);
        return result != null ? result : 0;
    }

    public int getRandomTransactionAmountV1(UUID user) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT AMOUNT FROM transactions ORDER BY USER_ID DESC LIMIT 1",
                Integer.class);
        return result != null ? result : 0;
    }
}
