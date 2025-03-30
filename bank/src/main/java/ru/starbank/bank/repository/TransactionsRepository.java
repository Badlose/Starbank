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
                "SELECT AMOUNT FROM transactions t WHERE t.USER_ID = ? LIMIT 1",
                Integer.class,
                userId);
        return result != null ? result : 0;
    }

    public int checkTransactionSumCompareRule(UUID userId, Rule rule) {
//        todo
        Integer result = jdbcTemplate.queryForObject(
                "SELECT AMOUNT FROM transactions t WHERE t.USER_ID = ? LIMIT 1",
                Integer.class,
                userId);
        return result != null ? result : 0;
    }

    public int checkTransactionSumCompareDepositWithdrawRule(UUID userId, Rule rule) {
        //todo
        Integer result = jdbcTemplate.queryForObject(
                "SELECT AMOUNT FROM transactions t WHERE t.USER_ID = ? LIMIT 1",
                Integer.class,
                userId);
        return result != null ? result : 0;
    }














//    public String DebitUsing(UUID userId) {
//
//        return """
//                    SELECT COUNT(*)
//                    FROM TRANSACTIONS t
//                    INNER JOIN PRODUCTS p ON t.product_id = p.id
//                    WHERE t.USER_ID = ? AND p.TYPE = 'DEBIT'
//                """;
//    }
//
//    public String InvestNotUsing(UUID userId) {
//
//        return """
//                SELECT COUNT(*)
//                FROM TRANSACTIONS t
//                INNER JOIN PRODUCTS p ON t.product_id = p.id
//                WHERE t.USER_ID = ?
//                  AND p.TYPE = 'INVEST'
//                """;
//    }
//
//    public String CreditNotUsing(UUID userId) {
//
//        return """
//                SELECT COUNT(*)
//                FROM TRANSACTIONS t
//                INNER JOIN PRODUCTS p ON t.product_id = p.id
//                WHERE t.USER_ID = ?
//                  AND p.TYPE = 'CREDIT'
//                """;
//    }
//
//    public String SavingDeposit(UUID userId) {
//
//        return """
//                SELECT SUM(AMOUNT)
//                FROM TRANSACTIONS t
//                INNER JOIN PRODUCTS p ON t.product_id = p.id
//                WHERE t.USER_ID = ?
//                  AND p.TYPE = 'SAVING'
//                  AND t.TYPE = 'DEPOSIT'
//                """;
//    }
//
//    public String DebitDeposit(UUID userId) {
//
//        return """
//                SELECT SUM(AMOUNT)
//                FROM TRANSACTIONS t
//                INNER JOIN PRODUCTS p ON t.product_id = p.id
//                WHERE t.USER_ID = ?
//                  AND p.TYPE = 'DEBIT'
//                  AND t.TYPE = 'DEPOSIT'
//                """;
//    }
//
//    public String DebitWithdraw(UUID userId) {
//
//        return """
//                SELECT SUM(AMOUNT)
//                FROM TRANSACTIONS t
//                INNER JOIN PRODUCTS p ON t.product_id = p.id
//                WHERE t.USER_ID = ?
//                  AND p.TYPE = 'DEBIT'
//                  AND t.TYPE = 'WITHDRAW'
//                """;
//    }

}
