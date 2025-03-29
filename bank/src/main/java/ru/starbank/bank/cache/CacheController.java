package ru.starbank.bank.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CacheController {

    private final CacheService cacheService;
    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/cache-contents")
    public ResponseEntity<Void> getCacheContents() {
        logger.info("Fetching cache contents");
        cacheService.printCacheContents();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/clear-cache")
    public ResponseEntity<Void> clearCache() {
        logger.info("Clearing cache");
        cacheService.clearCache();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cache-element/{key}")
    public ResponseEntity<Object> getCacheElement(@PathVariable String key) {
        logger.info("Fetching cache element with key: {}", key);
        Object value = cacheService.getElementByKey(key);
        if (value == null) {
            logger.warn("Cache element not found for key: {}", key);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }

    @PostMapping("/cache-element")
    public ResponseEntity<Void> putCacheElement(@RequestParam String key, @RequestParam String value) {
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            logger.error("Bad request: key or value is empty");
            return ResponseEntity.badRequest().build(); // Возвращаем 400, если ключ или значение пусты
        }
        logger.info("Adding cache element with key: {} and value: {}", key, value);
        cacheService.putElement(key, value);
        return ResponseEntity.ok().build();
    }

    // Обработка исключений
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        logger.error("Runtime exception occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}


