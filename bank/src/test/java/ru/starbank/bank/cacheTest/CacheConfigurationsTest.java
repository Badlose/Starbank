package ru.starbank.bank.cacheTest;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableCaching
public class CacheConfigurationsTest {

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {

        CaffeineCacheManager cacheManager = new CaffeineCacheManager("transactionCounts");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .recordStats());
        this.cacheManager = cacheManager;
    }

    @Test
    public void testCacheManager() {
        assertThat(cacheManager).isNotNull();

        CaffeineCache transactionCountsCache = (CaffeineCache) cacheManager.getCache("transactionCounts");
        assertThat(transactionCountsCache).isNotNull();

        transactionCountsCache.put("key1", 100);
        assertThat(transactionCountsCache.get("key1").get()).isEqualTo(100);
        assertThat(transactionCountsCache.get("key2")).isNull();
    }

    @Test
    public void testCacheExpiration() throws InterruptedException {
        CaffeineCache transactionCountsCache = (CaffeineCache) cacheManager.getCache("transactionCounts");

        transactionCountsCache.put("key1", 100);
        assertThat(transactionCountsCache.get("key1").get()).isEqualTo(100);


        Thread.sleep(2000);

        assertThat(transactionCountsCache.get("key1")).isNull();
    }
}

