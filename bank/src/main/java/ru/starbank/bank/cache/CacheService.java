package ru.starbank.bank.cache;

import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private final CacheManager cacheManager;
    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void printCacheContents() {
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache("recommendationCache").getNativeCache();
        if (cache != null) {
            cache.asMap().forEach((key, value) -> {
                logger.info("Key: {}, Value: {}", key, value);
            });
        } else {
            logger.info("Кэш не найден.");
        }
    }

    public void clearCache() {
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache("recommendationCache").getNativeCache();
        if (cache != null) {
            cache.invalidateAll();
            logger.info("Кэш очищен.");
        } else {
            logger.info("Кэш не найден.");
        }
    }

    public Object getElementByKey(Object key) {
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache("recommendationCache").getNativeCache();
        if (cache != null) {
            Object value = cache.getIfPresent(key); // Получаем элемент по ключу
            if (value != null) {
                logger.info("Элемент найден: Key = {}, Value = {}", key, value);
            } else {
                logger.warn("Элемент не найден для ключа: {}", key);
            }
            return value;
        } else {
            logger.info("Кэш не найден.");
            return null;
        }
    }

    public void putElement(Object key, Object value) {
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache("recommendationCache").getNativeCache();
        if (cache != null) {
            cache.put(key, value); // Добавляем или обновляем элемент
            logger.info("Элемент добавлен/обновлен: Key = {}, Value = {}", key, value);
        } else {
            logger.info("Кэш не найден.");
        }
    }
}
