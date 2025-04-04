package ru.starbank.bank.repository;

import ru.starbank.bank.exceptions.IllegalResultException;
import ru.starbank.bank.exceptions.SqlRequestException;
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

        Integer result = 0;

        try {
            result = jdbcTemplate.queryForObject(sql, Integer.class, userIdString, productType);
        } catch (SqlRequestException e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SqlRequestException("Error executing query.");
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {}.", userIdString, productType);
            throw new IllegalResultException();
        }

        logger.debug("Query returned: {}", result);
        return result;
    }

    public int compareTransactionSumByUserIdProductType(UUID userId, String productType, String transactionType) {

        String userIdString = userId.toString();
        String sql = """
                SELECT COALESCE(SUM(t.AMOUNT), 0)
                FROM TRANSACTIONS t
                INNER JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.user_ID = ?
                AND p.TYPE = ?
                AND t.TYPE = ?
                """;

        Integer result;

        try {
            result = jdbcTemplate.queryForObject(sql, Integer.class, userIdString, productType, transactionType);
            logger.info("RESULT {}", result);
        } catch (RuntimeException e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SqlRequestException("Error executing query.");
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {} and transaction type {}, {}.",
                    userIdString,
                    productType,
                    transactionType,
                    result);
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
        } catch (SqlRequestException e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SqlRequestException("Error executing query.");
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {}.", userIdString, productType);
            throw new IllegalResultException();
        }

        return result;
    }




}
