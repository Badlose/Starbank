package ru.starbank.bank.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import ru.starbank.bank.configuration.RecommendationsDataSourceConfiguration;

import java.util.UUID;


//@TestPropertySource("/application.properties")
@Import(value = RecommendationsDataSourceConfiguration.class)
public class TransactionsRepositoryTests {

    private final TransactionsRepository repository;
    private final RecommendationsDataSourceConfiguration configuration;
    public TransactionsRepositoryTests() {
        this.configuration = new RecommendationsDataSourceConfiguration();
        this.repository = new TransactionsRepository(configuration.recommendationsJdbcTemplate(configuration.recommendationsDataSource("jdbc:h2:file:./transactionTests")));
    }

    @Test
    public void shouldReturnCountTransactionsByUserIdProductType() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        String productType = "DEBIT";
        int expectedCount = 3;

        int actualCount = repository.countTransactionsByUserIdProductType(userId,productType);

        Assertions.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void shouldReturnCompareTransactionSumByUserIdProductType() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        String productType = "CREDIT";
        String transactionType = "DEPOSIT";
        int expectedSum = 93972;

        int actualSum = repository.compareTransactionSumByUserIdProductType(userId,productType,transactionType);

        Assertions.assertEquals(expectedSum, actualSum);
    }

    @Test
    public void shouldReturnCompareTransactionSumByUserIdProductTypeDepositWithdraw() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        String productType = "CREDIT";
        String comparison = "<";
        int expectedValue = 1;

        int actualValue = repository.compareTransactionSumByUserIdProductTypeDepositWithdraw(userId,productType,comparison);

        Assertions.assertEquals(expectedValue,actualValue);
    }
    @Test
    public void shouldReturnGetFirstNameLastNameByUserName(){
        String UserName = "quintin.deckow";
        String expectedUserFirstNameLastName = "Rolf Bogisich";

        String actualUserFirstNameLastName = repository.getFirstNameLastNameByUserName(UserName);

        Assertions.assertEquals(expectedUserFirstNameLastName,actualUserFirstNameLastName);
    }


    @Test
    public void shouldReturnGetUserIdByUserName(){
        String UserName = "quintin.deckow";
        UUID expectedId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

        UUID actualId = repository.getUserIdByUserName(UserName);

        Assertions.assertEquals(expectedId,actualId);
    }
}
