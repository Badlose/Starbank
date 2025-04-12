package ru.starbank.bank.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Import;
import ru.starbank.bank.configuration.CacheConfigurations;
import ru.starbank.bank.configuration.RecommendationsDataSourceConfiguration;
import ru.starbank.bank.exceptions.IllegalResultException;
import ru.starbank.bank.exceptions.SqlRequestException;
import ru.starbank.bank.service.Impl.ManagementServiceImpl;

import java.util.UUID;

//@SpringBootTest
//@TestPropertySource("/application.properties")
@Import(value = RecommendationsDataSourceConfiguration.class)
public class TransactionsRepositoryTests {
    private static final Logger logger = LoggerFactory.getLogger(TransactionsRepositoryTests.class);
    private final TransactionsRepository repository;
    @Autowired
    private final CacheManager cacheManager;
    private final CacheConfigurations cacheConfigurations;
    private final RecommendationsDataSourceConfiguration dataSourceConfiguration;

    public TransactionsRepositoryTests() {
        this.dataSourceConfiguration = new RecommendationsDataSourceConfiguration();
        this.repository = new TransactionsRepository(dataSourceConfiguration.recommendationsJdbcTemplate(dataSourceConfiguration.recommendationsDataSource("jdbc:h2:file:./transactionTests")));
        this.cacheConfigurations = new CacheConfigurations();
        this.cacheManager = cacheConfigurations.cacheManager();
    }

    public boolean cacheIsEmpty(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        Object nativeCache = cache.getNativeCache();
        if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache) {
            com.github.benmanes.caffeine.cache.Cache caffeineCache = (com.github.benmanes.caffeine.cache.Cache) nativeCache;
            return caffeineCache.estimatedSize() == 0;
        } else {
            return false;
        }
    }


    @Test
    public void shouldReturnCountTransactionsByUserIdProductType() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        String productType = "DEBIT";
        int expectedCount = 3;

        int actualCount = repository.countTransactionsByUserIdProductType(userId, productType);

        Assertions.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void shouldReturnCompareTransactionSumByUserIdProductType() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        String productType = "CREDIT";
        String transactionType = "DEPOSIT";
        int expectedSum = 93972;

        int actualSum = repository.compareTransactionSumByUserIdProductType(userId, productType, transactionType);

        Assertions.assertEquals(expectedSum, actualSum);
    }

    @Test
    public void shouldReturnCompareTransactionSumByUserIdProductTypeDepositWithdraw() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        String productType = "CREDIT";
        String comparison = "<";
        int expectedValue = 1;

        int actualValue = repository.compareTransactionSumByUserIdProductTypeDepositWithdraw(userId, productType, comparison);

        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void shouldReturnGetFirstNameLastNameByUserName() {
        String UserName = "quintin.deckow";
        String expectedUserFirstNameLastName = "Rolf Bogisich";

        String actualUserFirstNameLastName = repository.getFirstNameLastNameByUserName(UserName);

        Assertions.assertEquals(expectedUserFirstNameLastName, actualUserFirstNameLastName);
    }


    @Test
    public void shouldReturnGetUserIdByUserName() {
        String UserName = "quintin.deckow";
        UUID expectedId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

        UUID actualId = repository.getUserIdByUserName(UserName);

        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    public void shouldReturnThrowCountTransactionsByUserIdProductType() {
        UUID userId = null;//UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        String productType = "DEBIT"; //могут быть любыми

        Assertions.assertThrows(SqlRequestException.class, () -> repository.countTransactionsByUserIdProductType(userId, productType));
    }

    @Test
    public void shouldReturnThrowCompareTransactionSumByUserIdProductType() {
        UUID userId = null;
        String productType = "CREDIT"; //могут быть любыми
        String transactionType = "DEPOSIT"; //могут быть любыми

        Assertions.assertThrows(SqlRequestException.class, () -> repository.compareTransactionSumByUserIdProductType(userId, productType, transactionType));
    }

    @Test
    public void shouldReturnThrow1CompareTransactionSumByUserIdProductTypeDepositWithdraw() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        String productType = null; //могут быть любыми
        String comparison = ">";

        Assertions.assertDoesNotThrow(() -> repository.compareTransactionSumByUserIdProductTypeDepositWithdraw(userId, productType, comparison));

        UUID userId1 = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        String productType1 = "CREDIT"; //могут быть любыми
        String comparison1 = null;

        Assertions.assertThrows(SqlRequestException.class, () -> repository.compareTransactionSumByUserIdProductTypeDepositWithdraw(userId1, productType1, comparison1));

        UUID userId2 = null;
        String productType2 = "CREDIT"; //могут быть любыми
        String comparison2 = ">";

        Assertions.assertThrows(SqlRequestException.class, () -> repository.compareTransactionSumByUserIdProductTypeDepositWithdraw(userId2, productType2, comparison2));
    }

    @Test
    public void shouldReturnThrowGetFirstNameLastNameByUserName() {
        String UserName = null;

        Assertions.assertThrows(SqlRequestException.class, () -> repository.getFirstNameLastNameByUserName(UserName));

        String UserName1 = "344";

        Assertions.assertThrows(SqlRequestException.class, () -> repository.getFirstNameLastNameByUserName(UserName1));

        //String UserName2 = "assd.dff";
        //выбрасывает только SqlRequestException в случае если не нашел юзера
        //Assertions.assertThrows(IllegalResultException.class,()->repository.getFirstNameLastNameByUserName(UserName1));
    }


    @Test
    public void shouldReturnThrowGetUserIdByUserName() {
        String UserName = null;

        Assertions.assertThrows(SqlRequestException.class, () -> repository.getUserIdByUserName(UserName));

        String UserName1 = "344";

        Assertions.assertThrows(SqlRequestException.class, () -> repository.getUserIdByUserName(UserName1));

    }


}
