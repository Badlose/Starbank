package ru.starbank.bank.repository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.starbank.bank.configuration.RecommendationsDataSourceConfiguration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@EnableCaching
public class TransactionsRepositoryTestCache {
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private TransactionsRepository repository;
    private final RecommendationsDataSourceConfiguration dataSourceConfiguration;

    public TransactionsRepositoryTestCache() {
        this.dataSourceConfiguration = new RecommendationsDataSourceConfiguration();
        this.repository = new TransactionsRepository(dataSourceConfiguration.
                recommendationsJdbcTemplate(dataSourceConfiguration.
                        recommendationsDataSource("jdbc:h2:file:./transaction")));
    }

    @Test
    public void shouldCheckCache() {
        String sql = """
                SELECT COUNT(*)
                        FROM TRANSACTIONS t
                        INNER JOIN PRODUCTS p ON t.product_id = p.id
                        WHERE t.USER_ID = ?
                        AND p.TYPE = ?
                """;
        String userIdString = "cd515076-5d8a-44be-930e-8d4fcb79f42d";
        String productType = "DEBIT";

        int first = repository.countTransactionsByUserIdProductType(UUID.fromString(userIdString), productType);

        when(jdbcTemplate.queryForObject(sql, Integer.class, userIdString, productType)).thenReturn(2);
        int second = repository.countTransactionsByUserIdProductType(UUID.fromString(userIdString), productType);

        assertEquals(first, second);
    }

}
