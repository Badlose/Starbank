//package ru.starbank.bank.cache;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ru.starbank.bank.dto.DynamicRecommendationDTO;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/cache") // Группируем все маршруты под /api/cache
//public class CacheController {
//
//    private final CacheService cacheService;
//    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);
//
//    public CacheController(CacheService cacheService) {
//        this.cacheService = cacheService;
//    }
//
//    /**
//     * метод используется для получения содержимого кэша.
//     * Он вызывает метод printCacheContents() из cacheService, который, вероятно, выводит текущие элементы кэша в лог или консоль
//     *
//     * @GetMapping("/contents") Возвращает статус 200 (OK) без тела ответа, так как метод не возвращает никаких данных.
//     */
//    @GetMapping
//    public ResponseEntity<Void> getCacheContents() {
//        logger.info("Fetching cache contents");
//        cacheService.printCacheContents();
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * метод очищает кэш, вызывая метод clearCache() из cacheService
//     * Возвращает статус 200 (OK) без тела ответа, подтверждая успешное выполнение операции
//     *
//     * @return
//     */
//
//    @DeleteMapping("/clear")
//    public ResponseEntity<Void> clearCache() {
//        logger.info("Clearing cache");
//        cacheService.clearCache();
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * метод используется для получения элемента из кэша по заданному ключу. Ключ передается как часть URL.
//     * Логика:
//     * Метод сначала логирует запрос на получение элемента.
//     * Затем он вызывает getElementByKey(key) из cacheService, чтобы получить значение по ключу.
//     * Если значение не найдено (т.е. возвращается null), метод логирует предупреждение и возвращает статус 404 (NOT FOUND).
//     * Если элемент найден, он возвращается в ответе с кодом 200 (OK).
//     * Возвращает DynamicRecommendationDTO в случае успеха или статус 404, если элемент не найден.
//     *
//     * @param key
//     * @return
//     */
//    @GetMapping("/element/{key}")
//    public ResponseEntity<DynamicRecommendationDTO> getCacheElement(@PathVariable UUID key) {
//        logger.info("Fetching cache element with key: {}", key);
//        DynamicRecommendationDTO value = (DynamicRecommendationDTO) cacheService.getElementByKey(key);
//        if (value == null) {
//            logger.warn("Cache element not found for key: {}", key);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.ok(value);
//    }
//
//    /**
//     * метод добавляет новый элемент в кэш. Он принимает идентификатор продукта и объект DynamicRecommendationDTO в теле запроса.
//     * Логика:
//     * Метод проверяет, что productId и recommendation не равны null. Если один из них равен null, он логирует ошибку и возвращает статус 400 (BAD REQUEST).
//     * Если данные корректны, метод логирует информацию о добавлении элемента и вызывает putElement(productId, recommendation) из cacheService, чтобы сохранить
//     * элемент в кэше.
//     *
//     * @param productId
//     * @param recommendation
//     * @return
//     */
//
//    @PostMapping("/element")
//    public ResponseEntity<Void> putCacheElement(@RequestParam UUID productId, @RequestBody DynamicRecommendationDTO recommendation) {
//        if (productId == null || recommendation == null) {
//            logger.error("Bad request: productId or recommendation is null");
//            return ResponseEntity.badRequest().build();
//        }
//        logger.info("Adding cache element with productId: {} and recommendation: {}", productId, recommendation);
//        cacheService.putElement(productId, recommendation);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
//    /**
//     * Обработчик исключений
//     * метод предназначен для обработки исключений типа RuntimeException, которые могут возникнуть в любом из методов контроллера.
//     * Логика:
//     * Когда происходит исключение, метод логирует сообщение об ошибке с помощью logger.error, чтобы разработчики могли видеть, что именно пошло не так.
//     * Затем метод возвращает ответ с кодом состояния 500 (INTERNAL SERVER ERROR) и текстом ошибки в теле ответа. Это позволяет клиенту понять, что произошла
//     * ошибка, и получить информацию о ней.
//     * Возвращает статус 500 с сообщением об ошибке, что помогает в отладке и информировании клиента о проблеме.
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
//        logger.error("Runtime exception occurred: {}", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
//    }
//}
