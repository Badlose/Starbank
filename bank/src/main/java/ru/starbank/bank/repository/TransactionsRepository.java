package ru.starbank.bank.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.starbank.bank.exceptions.IllegalResultException;
import ru.starbank.bank.exceptions.SqlRequestException;

import java.util.UUID;

@Repository
public class TransactionsRepository {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public TransactionsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Cacheable(value = "transactionCounts")
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
        } catch (Exception e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SqlRequestException("Error executing query countTransactionsByUserIdProductType.");
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {}.", userIdString, productType);
            throw new IllegalResultException();
        }

        logger.info("Query returned: {}", result);
        return result;
    }

    @Cacheable(value = "transactionSumCompare")
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
        } catch (Exception e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SqlRequestException("Error executing query compareTransactionSumByUserIdProductType.");
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {} and transaction type {}.",
                    userIdString,
                    productType,
                    transactionType);
            throw new IllegalResultException();
        }

        logger.info("Query returned: {}", result);
        return result;
    }

    @Cacheable(value = "transactionSumCompareDepositWithdraw")
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
        } catch (Exception e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SqlRequestException("Error executing query compareTransactionSumByUserIdProductTypeDepositWithdraw.");
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {}.", userIdString, productType);
            throw new IllegalResultException();
        }

        logger.info("Query returned: {}", result);
        return result;
    }

    @CacheEvict(value = {"transactionCounts", "transactionSumCompare", "transactionSumCompareDepositWithdraw"}, allEntries = true)
    public void clearCache() {
        logger.info("clearCache - Clearing the entire cache");
    }

    public String getFirstNameLastNameByUserName(String userName) {
        String sql = "SELECT FIRST_NAME || ' ' || LAST_NAME AS FULL_NAME FROM USERS u WHERE u.USERNAME = ?";
        String userFirstNameLastName;
        try {
            userFirstNameLastName = jdbcTemplate.queryForObject(sql, String.class, userName);
        } catch (Exception e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SqlRequestException("Error executing query getFirstNameLastNameByUserName");
        }

        if (userFirstNameLastName == null) {
            logger.warn("Query returned null for username {}.", userName);
            throw new IllegalResultException();
        }

        logger.info("Query returned: {}", userFirstNameLastName);
        return userFirstNameLastName;
    }

    public UUID getUserIdByUserName(String userName) {
        String sql = "SELECT ID FROM USERS WHERE USERNAME = ?";
        UUID userId;
        try {
            userId = jdbcTemplate.queryForObject(sql, UUID.class, userName);
        } catch (Exception e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new SqlRequestException("Error executing query getUserIdByUserName");
        }
        logger.info("Query returned: {}", userId);
        return userId;
    }
}

