package ru.starbank.bank.cacheTest;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import ru.starbank.bank.cache.CacheController;
//import ru.starbank.bank.cache.CacheService;
//
//public class CacheServiceTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private CacheService cacheService;
//
//    @InjectMocks
//    private CacheController cacheController;
//
//    //setUp(): Метод, который выполняется перед каждым тестом. Он инициализирует моки и настраивает MockMvc
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(cacheController).build();
//    }
//
//    /**testGetCacheContents(): Проверяет, что метод printCacheContents вызывается и возвращает статус 200
//    *Чтобы убедиться, что метод getCacheContents контроллера корректно обрабатывает запрос
//    * и вызывает метод printCacheContents у сервиса кэша
//     */
//    @Test
//    public void testGetCacheContents() throws Exception {
//        mockMvc.perform(get("/cache-contents"))
//                .andExpect(status().isOk());
//
//        verify(cacheService, times(1)).printCacheContents();
//    }
//
//    /**testClearCache(): Проверяет, что метод clearCache вызывается и возвращает статус 200.
//    *Чтобы убедиться, что метод clearCache контроллера корректно обрабатывает запрос
//    * и вызывает метод clearCache у сервиса кэша.
//     */
//    @Test
//    public void testClearCache() throws Exception {
//        mockMvc.perform(get("/clear-cache"))
//                .andExpect(status().isOk());
//
//        verify(cacheService, times(1)).clearCache();
//    }
//
//    /**testGetCacheElementFound(): Проверяет, что элемент кэша найден, и возвращает его значение
//    *Чтобы  Убедиться, что метод getCacheElement корректно обрабатывает запрос, находит элемент по ключу
//    * и возвращает его значение.
//     */
//    @Test
//    public void testGetCacheElementFound() throws Exception {
//        String key = "testKey";
//        String value = "testValue";
//
//        when(cacheService.getElementByKey(key)).thenReturn(value);
//
//        mockMvc.perform(get("/cache-element/{key}", key))
//                .andExpect(status().isOk())
//                .andExpect(content().string(value));
//
//        verify(cacheService, times(1)).getElementByKey(key);
//    }
//
//    /**отправляет GET-запрос на /cache-element/testKey и проверяет, что возвращается статус 404 (Not Found)
//    * чтобы Убедиться, что метод getCacheElement корректно обрабатывает случай, когда элемент не найден,
//    * и возвращает соответствующий статус
//     */
//    @Test
//    public void testGetCacheElementNotFound() throws Exception {
//        String key = "testKey";
//
//        when(cacheService.getElementByKey(key)).thenReturn(null);
//
//        mockMvc.perform(get("/cache-element/{key}", key))
//                .andExpect(status().isNotFound());
//
//        verify(cacheService, times(1)).getElementByKey(key);
//    }
//
//    /** тест отправляет POST-запрос на /cache-element с параметрами key и value, и проверяет, что возвращается статус 200 (OK).
//    *чтобы Убедиться, что метод putCacheElement корректно обрабатывает запрос на добавление элемента в кэш
//    * и вызывает соответствующий метод сервиса.
//     */
//    @Test
//    public void testPutCacheElementSuccess() throws Exception {
//        String key = "testKey";
//        String value = "testValue";
//
//        mockMvc.perform(post("/cache-element")
//                        .param("key", key)
//                        .param("value", value))
//                .andExpect(status().isOk());
//
//        verify(cacheService, times(1)).putElement(key, value);
//    }
//    /**тест отправляет два POST-запроса на /cache-element. Первый запрос имеет пустое значение для параметра key,
//    * а второй — пустое значение для параметра value. Оба запроса проверяют, что возвращается статус 400 (Bad Request).
//    *чтобы убедиться, что метод putCacheElement корректно обрабатывает ошибки при отсутствии обязательных параметров
//    * и возвращает соответствующий статус
//     */
//
//    @Test
//    public void testPutCacheElementBadRequest() throws Exception {
//        mockMvc.perform(post("/cache-element")
//                        .param("key", "")
//                        .param("value", "testValue"))
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(post("/cache-element")
//                        .param("key", "testKey")
//                        .param("value", ""))
//                .andExpect(status().isBadRequest());
//    }
//
//    /** тест имитирует ситуацию, когда метод getElementByKey выбрасывает исключение RuntimeException.
//    * Он отправляет GET-запрос на /cache-element/testKey и проверяет, что возвращается статус 500 (Internal Server Error)
//    * и сообщение содержит текст "Test Exception".
//    *чтобы убедиться, что контроллер корректно обрабатывает исключения, возникающие в сервисе,
//    * и возвращает соответствующий статус и сообщение. Это важно для обеспечения устойчивости приложения
//    * и информирования пользователя о возникших ошибках.
//     */
//    @Test
//    public void testHandleRuntimeException() throws Exception {
//        when(cacheService.getElementByKey(anyString())).thenThrow(new RuntimeException("Test Exception"));
//
//        mockMvc.perform(get("/cache-element/{key}", "testKey"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string("Test Exception"));
//    }
//}
//
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

    @Test
    public void testCacheExpiration() throws InterruptedException {
        CaffeineCache transactionCountsCache = (CaffeineCache) cacheManager.getCache("transactionCounts");

        // Добавляем значение в кэш
        transactionCountsCache.put("key1", 100);

        // Проверяем, что значение доступно
        assertThat(transactionCountsCache.get("key1").get()).isEqualTo(100);

        // Ждем, пока кэш истечет (10 минут)
        Thread.sleep(1000 * 60 * 10 + 1000); // 10 минут + 1 секунда

        // Проверяем, что значение истекло
        assertThat(transactionCountsCache.get("key1")).isNull();
    }
}
