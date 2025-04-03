package ru.starbank.bank.repository;

import ru.starbank.bank.exceptions.IllegalResultException;
import ru.starbank.bank.exceptions.SQLRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class TransactionsRepository {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public TransactionsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int countTransactionsByUserIdProductType(UUID userId, String productType) {

        String sql = """
                SELECT COUNT(*)
                        FROM TRANSACTIONS t
                        INNER JOIN PRODUCTS p ON t.product_id = p.id
                        WHERE t.USER_ID = ?
                        AND p.TYPE = ?
                """;

        String userIdString = userId.toString();

        Integer result;

        try {
            result = jdbcTemplate.queryForObject(sql, Integer.class, userIdString, productType);
        } catch (SQLRequestException e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SQLRequestException("Error executing query.");
        }

        if (result == null) {
//            logger.warn("Query returned null for user {} and product type {}.  Returning -1.", userIdString, productType);
            throw new IllegalResultException();
        }

        logger.debug("Query returned: {}", result);
        return result;
    }

    public int compareTransactionSumByUserIdProductType(UUID userId, String productType, String transactionType) {

        String userIdString = userId.toString();
        String sql = """
                SELECT SUM(t.AMOUNT)
                FROM TRANSACTIONS t
                INNER JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.user_ID = ?
                AND p.TYPE = ?
                AND t.TYPE = ?
                """;

        Integer result = null;

        try {
            result = jdbcTemplate.queryForObject(sql, Integer.class, userIdString, productType, transactionType);
        } catch (SQLRequestException e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SQLRequestException("Error executing query.");
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {} and transaction type {}.  Returning -1.",
                    userIdString,
                    productType,
                    transactionType);
            throw new IllegalResultException();
        }

        logger.debug("Query returned: {}", result);
        return result;
    }

    public int compareTransactionSumByUserIdProductTypeDepositWithdraw(UUID userId, String productType, String comparison) {

        String userIdString = userId.toString();

        String sql = """
                SELECT
                CASE
                    WHEN (
                        SELECT COALESCE(SUM(t.AMOUNT), 0)
                        FROM TRANSACTIONS t
                        INNER JOIN PRODUCTS p ON t.product_id = p.id
                        WHERE t.USER_ID = ?
                          AND p.TYPE = ?
                          AND t.TYPE = 'DEPOSIT'
                    )""" + comparison +
                """
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
                        """;

        Integer result = null;
        try {
            result = jdbcTemplate.queryForObject(sql, Integer.class, userIdString, productType, userIdString, productType);
        } catch (SQLRequestException e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SQLRequestException("Error executing query.");
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {}.  Returning -1.", userIdString, productType);
            throw new IllegalResultException();
        }

        return result;
    }




}
