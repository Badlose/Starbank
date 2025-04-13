package ru.starbank.bank.cacheTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableCaching
public class CacheConfigurationsTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testCacheManager() {
        // Проверяем, что кэш-менеджер инициализирован
        assertThat(cacheManager).isNotNull();

        // Проверяем наличие кэша "transactionCounts"
        CaffeineCache transactionCountsCache = (CaffeineCache) cacheManager.getCache("transactionCounts");
        assertThat(transactionCountsCache).isNotNull();

        // Добавляем значение в кэш
        transactionCountsCache.put("key1", 100);

        // Проверяем, что значение корректно извлекается из кэша
        assertThat(transactionCountsCache.get("key1").get()).isEqualTo(100);

        // Проверяем, что значение отсутствует для несуществующего ключа
        assertThat(transactionCountsCache.get("key2")).isNull();
    }

//      @Test
//    public void testCacheExpiration() throws InterruptedException {
//        CaffeineCache transactionCountsCache = (CaffeineCache) cacheManager.getCache("transactionCounts");
//
//        // Добавляем значение в кэш
//        transactionCountsCache.put("key1", 100);
//
//        // Проверяем, что значение доступно
//        assertThat(transactionCountsCache.get("key1").get()).isEqualTo(100);
//
//        // Ждем, пока кэш истечет (10 минут)
//        Thread.sleep(1000 * 60 * 10 + 1000); // 10 минут + 1 секунда
//
//        // Проверяем, что значение истекло
//        assertThat(transactionCountsCache.get("key1")).isNull();
//    }
}
