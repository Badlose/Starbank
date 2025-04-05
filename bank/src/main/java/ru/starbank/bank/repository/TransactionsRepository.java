package ru.starbank.bank.repository;

import org.checkerframework.checker.optional.qual.Present;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.starbank.bank.model.Rule;

import java.util.List;
import java.util.UUID;

@Repository
public class TransactionsRepository {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public TransactionsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int checkUserOfRule(UUID userId, Rule rule) {
        if (rule == null || rule.getArguments() == null || rule.getArguments().isEmpty()) {
            logger.warn("Rule or rule arguments are null or empty.  Returning -1.");
            return -1;
        }

        List<String> arguments = rule.getArguments();
        String productType = arguments.get(0);

        if (arguments == null || productType.isEmpty()) {
            logger.warn("Product type is null or empty. Returning -1.");
            return -1;
        }

        String sql = """
                SELECT COUNT(*)
                        FROM TRANSACTIONS t
                        INNER JOIN PRODUCTS p ON t.product_id = p.id
                        WHERE t.USER_ID = ?
                        AND p.TYPE = ?
                """;

        String userIdString = userId.toString();

        Integer result = null;

        try {
            result = jdbcTemplate.queryForObject(sql, Integer.class, userIdString, productType);
        } catch (Exception e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            return -1;
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {}.  Returning -1.", userIdString, productType);
            return -1;
        }

        logger.debug("Query returned: {}", result);
        return result;
    }

    public int checkTransactionSumCompareRule(UUID userId, Rule rule) {
        if (rule == null || rule.getArguments() == null || rule.getArguments().isEmpty()) {
            logger.warn("Rule or rule arguments are null or empty.  Returning -1.");
            return -1;
        }

        List<String> arguments = rule.getArguments();
        String productType = arguments.get(0);
        String transactionType = arguments.get(1);

        if (arguments == null || productType.isEmpty() || transactionType.isEmpty()) {
            logger.warn("Product type or Transaction type are null or empty. Returning -1.");
            return -1;
        }
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
        } catch (Exception e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            return -1;
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {} and transaction type {}.  Returning -1.",
                    userIdString,
                    productType,
                    transactionType);
            return -1;
        }

        logger.debug("Query returned: {}", result);
        return result;
    }

    public int checkTransactionSumCompareDepositWithdrawRule(UUID userId, Rule rule) {

        if (rule == null || rule.getArguments() == null || rule.getArguments().isEmpty()) {
            logger.warn("Rule or rule arguments are null or empty.  Returning -1.");
            return -1;
        }

        List<String> arguments = rule.getArguments();
        String productType = arguments.get(0);
        String comparison = rule.getArguments().get(1);

        if (arguments == null || productType.isEmpty() || comparison.isEmpty()) {
            logger.warn("Product type or Comparison type are null or empty. Returning -1.");
            return -1;
        }

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
            return -1;
        }

        if (result == null) {
            logger.warn("Query returned null for user {} and product type {}.  Returning -1.", userIdString, productType);
            return -1;
        }

        return result;
    }
    public UUID getUserIdByUserName(String userName) {
        String sql = "SELECT id FROM USERS WHERE username = ?";
        UUID userId;

        try {
         userId=jdbcTemplate.queryForObject(sql, UUID.class,userName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return userId;
    }
    public String getFirstNameLastNameByUserName(String userName) {
        String sql = "SELECT first_name  ' '  last_name AS full_name FROM USERS u WHERE u.username = ?";
        String userFirstNameLastName;
        try {
            userFirstNameLastName= jdbcTemplate.queryForObject(sql, String.class,userName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return userFirstNameLastName;
    }

}
