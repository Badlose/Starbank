package ru.starbank.bank.TestController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.starbank.bank.Controller.RecommendationController;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.RecommendationService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RecommendationControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationControllerTest.class);

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    private MockMvc mockMvc;

    private Recommendation recommendation;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
        UUID userId = UUID.randomUUID();
        logger.info("Настройка теста с помощью userId: {}", userId);

        recommendation = new Recommendation();
        recommendation.setId(UUID.randomUUID());
        recommendation.setName("Пример рекомендации");
        recommendation.setText("Это текст примера рекомендации.");
    }

    @Test
    public void shouldReturnsOkWithEmptyResponse() throws Exception {
        UUID userId = UUID.fromString("1f7dac2e-7186-400b-9bf1-64961ab1831a");

        when(recommendationService.getRecommendation(userId)).thenReturn(Collections.emptyList());

        logger.info("Настройка теста с помощью userId: {}", userId);

        mockMvc.perform(get("/recommendation/userId/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        logger.info("Запрос выполнен для userId: {}", userId);

        verify(recommendationService, times(1)).getRecommendation(userId);
        logger.info("Проверка, что метод getRecommendationsWithLogging был вызван для userId: {}", userId);
    }

    @Test
    public void shouldReturnsEmptyList() throws Exception {
        UUID userId = UUID.fromString("a1b2c3d4-e5f6-4789-9abc-def012345678");

        logger.info("Тестирование getRecommendation с помощью userId : {}", userId);

        when(recommendationService.getRecommendation(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/recommendation/userId/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        logger.info("Завершенный тестGetRecommendation возвращает пустой список");
    }

    @Test
    public void shouldReturnsRecommendations() throws Exception {
        UUID userId = UUID.randomUUID();
        logger.info("Создание фиксированного UUID  userId: {}", userId);

        List<Recommendation> recommendationsList = Arrays.asList(recommendation);
        logger.info("Создан список рекомендаций для теста: {}", recommendationsList);

        when(recommendationService.getRecommendation(userId)).thenReturn(recommendationsList);
        logger.info("Настроено поведение мок-сервиса для userId: {}", userId);

        mockMvc.perform(get("/recommendation/userId/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Пример рекомендации"))
                .andExpect(jsonPath("$[0].text").value("Это текст примера рекомендации."));

        logger.info("Тест успешно завершен для userId: {}", userId);
    }

}




