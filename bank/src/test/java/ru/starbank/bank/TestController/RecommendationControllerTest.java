package ru.starbank.bank.TestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.starbank.bank.controller.RecommendationController;
import ru.starbank.bank.dto.UserDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.service.RecommendationService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RecommendationController.class)
@AutoConfigureMockMvc
public class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecommendationService recommendationService;

   // @InjectMocks
    //private RecommendationController recommendationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetRecommendation() throws Exception {
        // Создаем тестовый объект UserRecommendationsDTO
        UUID testUserId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        List<UserDTO> mockRecommendations = new ArrayList<>();
        mockRecommendations.add(new UserDTO()); // Пример добавления UserDTO
        UserRecommendationsDTO mockRecommendation = new UserRecommendationsDTO(testUserId, mockRecommendations);

        // Настройка мока для recommendationService
        when(recommendationService.getRecommendation(any(UUID.class))).thenReturn(mockRecommendation);

        // Выполнение GET-запроса и проверка статуса и содержимого ответа
        mockMvc.perform(get("/recommendation/userId/{userId}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUserId.toString())) // Проверка userId
                .andExpect(jsonPath("$.recommendations[0].name").value("John Doe")) // Проверка имени первого пользователя
                .andExpect(jsonPath("$.recommendations[0].email").value("john.doe@example.com")); // Проверка email первого пользователя
    }



    @Test
    public void testGetRecommendationNotFound() throws Exception {
        // Настройка мока для случая, когда рекомендация не найдена
        when(recommendationService.getRecommendation(any(UUID.class))).thenThrow(new RuntimeException("Recommendation not found"));

        // Выполнение GET-запроса и проверка статуса 204 No Content
        mockMvc.perform(get("/recommendation/userId/{userId}", "cd515076-5d8a-44be-930e-8d4fcb79f42d")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

