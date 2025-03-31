package ru.starbank.bank.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.starbank.bank.model.Rule;

import java.util.UUID;

@Repository
public class TransactionsRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int checkUserOfRule(UUID userId, Rule rule) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(*)
                                FROM TRANSACTIONS t
                                INNER JOIN PRODUCTS p ON t.product_id = p.id
                                WHERE t.USER_ID = ?
                                AND p.TYPE = ?
                        """,
                Integer.class,
                userId,
                rule.getArguments().get(0));
        return result != null ? result : -1;
    }

    public int checkTransactionSumCompareRule(UUID userId, Rule rule) {

        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT SUM(t.AMOUNT)
                        FROM TRANSACTIONS t
                        INNER JOIN PRODUCTS p ON t.product_id = p.id
                        WHERE t.user_ID = ?
                        AND p.TYPE = ?
                        AND t.TYPE = ?
                        """,
                Integer.class,
                userId,
                rule.getArguments().get(0),
                rule.getArguments().get(1));
        return result != null ? result : -1;
    }

    public int checkTransactionSumCompareDepositWithdrawRule(UUID userId, Rule rule) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT
                        CASE
                            WHEN (
                                SELECT COALESCE(SUM(t.AMOUNT), 0)
                                FROM TRANSACTIONS t
                                INNER JOIN PRODUCTS p ON t.product_id = p.id
                                WHERE t.USER_ID = ?
                                  AND p.TYPE = ?
                                  AND t.TYPE = 'DEPOSIT'
                            )""" + rule.getArguments().get(1) + """
                                (
                                SELECT COALESCE(SUM(t.AMOUNT), 0)
                                FROM TRANSACTIONS t
                                INNER JOIN PRODUCTS p ON t.product_id = p.id
                                WHERE t.USER_ID = ?
                                  AND p.TYPE = ?
                                  AND t.TYPE = 'WITHDRAW'
                            ) THEN 1
                            ELSE 0
                        END AS is_debit_deposits_greater_than_debit_withdrawals;
                        """,
                Integer.class,
                userId,
                rule.getArguments().get(0),
                userId,
                rule.getArguments().get(0));
        return result != null ? result : -1;
    }

}
