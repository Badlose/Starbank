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

    /**
     * Выводит содержимое кэша в лог.
     */
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

    /**
     * Очищает кэш.
     */
    public void clearCache() {
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache("recommendationCache").getNativeCache();
        if (cache != null) {
            cache.invalidateAll();
            logger.info("Кэш очищен.");
        } else {
            logger.info("Кэш не найден.");
        }
    }

    /**
     * Получает элемент по ключу из кэша.
     *
     * @param key Ключ элемента.
     * @return Значение элемента или null, если элемент не найден.
     */
    public Object getElementByKey(Object key) {
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache("recommendationCache").getNativeCache();
        if (cache != null) {
            Object value = cache.getIfPresent(key);
            if (value != null) {
                logger.info("Элемент найден: Key = {}, Value = {}", key, value);
            } else {
                logger.info("Элемент не найден для ключа: {}", key);
            }
            return value;
        } else {
            logger.info("Кэш не найден.");
            return null;
        }
    }

    /**
     * Добавляет или обновляет элемент в кэше.
     *
     * @param key   Ключ элемента.
     * @param value Значение элемента.
     */
    public void putElement(Object key, Object value) {
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache("recommendationCache").getNativeCache();
        if (cache != null) {
            cache.put(key, value);
            logger.info("Элемент добавлен/обновлен: Key = {}, Value = {}", key, value);
        } else {
            logger.info("Кэш не найден.");
        }
    }
}

