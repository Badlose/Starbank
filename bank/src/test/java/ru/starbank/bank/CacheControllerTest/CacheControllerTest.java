package ru.starbank.bank.CacheControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.starbank.bank.cache.CacheController;
import ru.starbank.bank.cache.CacheService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CacheController.class)
public class CacheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CacheService cacheService;

    // Тест для проверки успешного получения содержимого кеша
    @Test
    public void testGetCacheContents() throws Exception {
        mockMvc.perform(get("/cache-contents"))
                .andExpect(status().isOk());

        verify(cacheService, times(1)).printCacheContents();
    }

    // Тест для проверки успешного очищения кеша
    @Test
    public void testClearCache() throws Exception {
        mockMvc.perform(get("/clear-cache"))
                .andExpect(status().isOk());

        verify(cacheService, times(1)).clearCache();
    }

    // Тест для проверки успешного получения элемента кеша по ключу
    @Test
    public void testGetCacheElement() throws Exception {
        String key = "testKey";
        Object value = "testValue";
        when(cacheService.getElementByKey(key)).thenReturn(value);

        mockMvc.perform(get("/cache-element/{key}", key))
                .andExpect(status().isOk())
                .andExpect(content().string("testValue"));

        verify(cacheService, times(1)).getElementByKey(key);
    }

    // Тест для проверки ситуации, когда элемент кеша не найден
    @Test
    public void testGetCacheElementNotFound() throws Exception {
        String key = "nonExistentKey";
        when(cacheService.getElementByKey(key)).thenReturn(null); // Или выбросьте исключение

        mockMvc.perform(get("/cache-element/{key}", key))
                .andExpect(status().isNotFound()); // Предполагаем, что возвращаем статус 404

        verify(cacheService, times(1)).getElementByKey(key);
    }

    // Тест для проверки успешного добавления элемента в кеш
    @Test
    public void testPutCacheElement() throws Exception {
        String key = "testKey";
        String value = "testValue";

        mockMvc.perform(post("/cache-element")
                        .param("key", key)
                        .param("value", value)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        verify(cacheService, times(1)).putElement(key, value);
    }

    // Тест для проверки обработки ошибки при добавлении элемента с пустым ключом
    @Test
    public void testPutCacheElementWithEmptyKey() throws Exception {
        mockMvc.perform(post("/cache-element")
                        .param("key", "")
                        .param("value", "testValue")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest()); // Предполагаем, что возвращаем статус 400
    }

    // Тест для проверки обработки ошибки при добавлении элемента с пустым значением
    @Test
    public void testPutCacheElementWithNullValue() throws Exception {
        String key = "testKey";

        mockMvc.perform(post("/cache-element")
                        .param("key", key)
                        .param("value", "") // Здесь можно использовать пустую строку, если это допустимо
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest()); // Предполагаем, что возвращаем статус 400
    }

    // Тест для проверки обработки исключения при получении элемента кеша
    @Test
    public void testGetCacheElementThrowsException() throws Exception {
        String key = "testKey";
        when(cacheService.getElementByKey(key)).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/cache-element/{key}", key))
                .andExpect(status().isInternalServerError()) // Ожидаем 500 ошибку
                .andExpect(content().string("Service error")); // Проверяем сообщение об ошибке

        verify(cacheService, times(1)).getElementByKey(key);
    }
}


