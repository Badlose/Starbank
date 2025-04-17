package ru.starbank.bank.cacheTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableCaching
public class CacheConfigurationsTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testCacheManager() {
        assertThat(cacheManager).isNotNull();

        CaffeineCache transactionCountsCache = (CaffeineCache) cacheManager.getCache("transactionCounts");
        assertThat(transactionCountsCache).isNotNull();

        transactionCountsCache.put("key1", 100);

        assertThat(transactionCountsCache.get("key1").get()).isEqualTo(100);

        assertThat(transactionCountsCache.get("key2")).isNull();
    }

//    @Test
//    public void testCacheExpiration() throws InterruptedException {
//        CaffeineCache transactionCountsCache = (CaffeineCache) cacheManager.getCache("transactionCounts");
//
//        transactionCountsCache.put("key1", 100);
//
//        assertThat(transactionCountsCache.get("key1").get()).isEqualTo(100);
//
//        Thread.sleep(1000 * 60 * 10 + 1000);
//
//        assertThat(transactionCountsCache.get("key1")).isNull();
//    }

}
