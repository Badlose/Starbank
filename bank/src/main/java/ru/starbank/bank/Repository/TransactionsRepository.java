package ru.starbank.bank.Repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class TransactionsRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String DebitUsing(UUID userId) {

        return """
                    SELECT COUNT(*)
                    FROM TRANSACTIONS t
                    INNER JOIN PRODUCTS p ON t.product_id = p.id
                    WHERE t.USER_ID = ? AND p.TYPE = 'DEBIT'
                """;
    }

    public String InvestNotUsing(UUID userId) {

        return """
                SELECT COUNT(*)
                FROM TRANSACTIONS t
                INNER JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.USER_ID = ?
                  AND p.TYPE = 'INVEST'
                """;
    }

    public String CreditNotUsing(UUID userId) {

        return """
                SELECT COUNT(*)
                FROM TRANSACTIONS t
                INNER JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.USER_ID = ?
                  AND p.TYPE = 'CREDIT'
                """;
    }

    public String SavingDeposit(UUID userId) {

        return """
                SELECT SUM(AMOUNT)
                FROM TRANSACTIONS t
                INNER JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.USER_ID = ?
                  AND p.TYPE = 'SAVING'
                  AND t.TYPE = 'DEPOSIT'
                """;
    }

    public String DebitDeposit(UUID userId) {

        return """
                SELECT SUM(AMOUNT)
                FROM TRANSACTIONS t
                INNER JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.USER_ID = ?
                  AND p.TYPE = 'DEBIT'
                  AND t.TYPE = 'DEPOSIT'
                """;
    }

    public String DebitWithdraw(UUID userId) {

        return """
                SELECT SUM(AMOUNT)
                FROM TRANSACTIONS t
                INNER JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.USER_ID = ?
                  AND p.TYPE = 'DEBIT'
                  AND t.TYPE = 'WITHDRAW'
                """;
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
