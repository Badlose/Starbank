package ru.starbank.bank.TestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import ru.starbank.bank.dto.*;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Statistic;
import ru.starbank.bank.service.RecommendationService;

import java.util.*;

import static org.apache.http.client.methods.RequestBuilder.post;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@SpringBootTest
@AutoConfigureMockMvc
public class RecommendationControllerTest {


    @MockitoBean
    private RecommendationService recommendationService;

    @Autowired
    private MockMvc mockMvc;

    private DynamicRecommendationDTO dynamicRecommendationDTO;

    private DynamicRecommendation dynamicRecommendation;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
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
                .andExpect(status().isOk());

    }

    @Test
    public void testCreateNewDynamicRecommendation_Success() throws Exception {
        DynamicRecommendation recommendation = new DynamicRecommendation();
        recommendation.setId(1L);
        recommendation.setName("Invest 500");
        recommendation.setProductId(UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"));
        recommendation.setText("Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка!" +
                " Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и " +
                "получите выгоду в виде вычета на взнос в следующем налоговом периоде. " +
                "Не упустите возможность разнообразить свой портфель, снизить риски и" +
                " следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!");

        DynamicRecommendationDTO expectedDto = new DynamicRecommendationDTO();
        expectedDto.setId(1L);
        expectedDto.setProduct_name("Invest 500");
        expectedDto.setProduct_id(UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"));
        expectedDto.setProduct_text("Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка!" +
                " Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и " +
                "получите выгоду в виде вычета на взнос в следующем налоговом периоде. " +
                "Не упустите возможность разнообразить свой портфель, снизить риски и" +
                " следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!");

        // Настройка мока для метода сервиса
        when(recommendationService.createNewDynamicRecommendation(any(DynamicRecommendation.class))).thenReturn(expectedDto);

        // Выполнение запроса и проверка результатов
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recommendation/rule/Invest 500") // Путь к вашему контроллеру
                        .contentType(MediaType.APPLICATION_JSON) // Указание типа контента
                        .content(objectMapper.writeValueAsString(recommendation))) // Передача JSON-содержимого
                .andExpect(status().isOk()) // Проверка статуса ответа (200 Created)
                .andExpect(jsonPath("$.id").value(expectedDto.getId())) // Проверка ID в ответе
                .andExpect(jsonPath("$.product_name").value(expectedDto.getProduct_name())) // Проверка имени продукта
                .andExpect(jsonPath("$.product_id").value(expectedDto.getProduct_id().toString())) // Проверка ID продукта
                .andExpect(jsonPath("$.product_text").value(expectedDto.getProduct_text())); // Проверка текста продукта

        // Проверка, что метод сервиса был вызван
        verify(recommendationService).createNewDynamicRecommendation(any(DynamicRecommendation.class));
    }

    @Test
    public void testCreateNewDynamicRecommendation_InvalidRecommendation() throws Exception {
        String json = "{\"name\":null,\"productId\":3fa85f64-5717-4562-b3fc-2c963f66afa6,\"text\":null,\"ruleList\":null}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recommendation/rule/Invest 500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()); // Ожидаем статус 400
    }

//    @Test
//    public void testGetAllDynamicRecommendations() throws Exception {
//        List<DynamicRecommendationDTO> recommendationDTOs = new ArrayList<>();
//        recommendationDTOs.add(new DynamicRecommendationDTO(1L, "Recommendation 1", "Description for recommendation 1"));
//        recommendationDTOs.add(new DynamicRecommendationDTO(2L, "Recommendation 2", "Description for recommendation 2"));
//
//
//        // Создание объекта ListDynamicRecommendationDTO с заполненным списком
//        ListDynamicRecommendationDTO responseDto = new ListDynamicRecommendationDTO();
//        responseDto.setData(recommendationDTOs);
//        // Настройка поведения мока
//        when(recommendationService.getAllDynamicRecommendations()).thenReturn(responseDto);
//
//        // Выполнение запроса
//        mockMvc.perform(get("/recommendation/rule")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data").isNotEmpty())
//                .andExpect(jsonPath("$.data[0].product_name").value("Recommendation 1"))
//                .andExpect(jsonPath("$.data[1].product_name").value("Recommendation 2"));
//
//
//
//    }

    @Test
    public void testDeleteDynamicRecommendation() throws Exception {
        Long recommendationId = 1L;

        doNothing().when(recommendationService).deleteDynamicRecommendation(recommendationId);
        mockMvc.perform(delete("/recommendation/rule/{recommendationId}", recommendationId))
                .andExpect(status().isNoContent());

        verify(recommendationService).deleteDynamicRecommendation(recommendationId);


    }

    @Test
    public void testDeleteDynamicRecommendation_NotFound() throws Exception {
        Long recommendationId = 1L;

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(recommendationService).deleteDynamicRecommendation(recommendationId);

        mockMvc.perform(delete("/recommendation/rule/{recommendationId}", recommendationId))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void testGetStatistics_Success() throws Exception {
//
//        Statistic stat1 = new Statistic(1L, 10);
//        Statistic stat2 = new Statistic(2L, 5);
//        List<Statistic> statistics = Arrays.asList(stat1, stat2);
//
//        when(recommendationService.getStatistics()).thenReturn(statistics);
//        mockMvc.perform(get("/rule/stats")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(2))); // Проверка размера списка
//    }
}




