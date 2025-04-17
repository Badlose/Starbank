package ru.starbank.bank.TestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import ru.starbank.bank.dto.*;
import ru.starbank.bank.dto.mapper.DynamicRecommendationMapper;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.model.Statistic;
import ru.starbank.bank.service.RecommendationService;
import ru.starbank.bank.telegram.service.impl.MessageProcessorServiceImpl;

import java.util.*;

import static org.apache.http.client.methods.RequestBuilder.post;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Autowired
    private DynamicRecommendationMapper dynamicRecommendationMapper;

    private ObjectMapper objectMapper;

    private final Logger LOGGER = LoggerFactory.getLogger(RecommendationControllerTest.class);

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


        when(recommendationService.createNewDynamicRecommendation(any(DynamicRecommendation.class))).thenReturn(expectedDto);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recommendation/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedDto.getId()))
                .andExpect(jsonPath("$.product_name").value(expectedDto.getProduct_name()))
                .andExpect(jsonPath("$.product_id").value(expectedDto.getProduct_id().toString()))
                .andExpect(jsonPath("$.product_text").value(expectedDto.getProduct_text()));


        verify(recommendationService).createNewDynamicRecommendation(any(DynamicRecommendation.class));
    }


    @Test
    public void testCreateNewDynamicRecommendation_InvalidRecommendation() throws Exception {
        String json = "{\"name\":null,\"productId\":3fa85f64-5717-4562-b3fc-2c963f66afa6,\"text\":null,\"ruleList\":null}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recommendation/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()); // Ожидаем статус 400
    }

    @Test
    void testGetAllDynamicRecommendations() throws Exception {
        // Создайте несколько объектов DynamicRecommendationDTO


        UUID INVEST500ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
        String INVEST500NAME = "Invest500";
        String INVEST500TEXT = "\nОткройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! " +
                "Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. " +
                "Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

        UUID TOPSAVINGID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
        String TOPSAVINGNAME = "TopSaving";
        String TOPSAVINGTEXT = """

                Откройте свою собственную «Копилку» с нашим банком! \
                «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. \
                Больше никаких забытых чеков и потерянных квитанций — всё под контролем!

                Преимущества «Копилки»:

                Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.

                Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.

                Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.

                Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""";

        List<Rule> INVEST500RULES = new ArrayList<>(List.of(
                new Rule("USER_OF", List.of("DEBIT"), true),
                new Rule("USER_OF", List.of("INVEST"), false),
                new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000"), true)
        ));
        List<Rule> TOPSAVINGRULES = new ArrayList<>(List.of(
                new Rule("USER_OF", List.of("DEBIT"), true),
                new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">=", "50000", "OR"), true),
                new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000", "OR"), true),
                new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true) // +
        ));
        DynamicRecommendation invest500DR = new DynamicRecommendation(INVEST500NAME, INVEST500ID, INVEST500TEXT, INVEST500RULES);
        DynamicRecommendation topSavingDR = new DynamicRecommendation(TOPSAVINGNAME, TOPSAVINGID, TOPSAVINGTEXT, TOPSAVINGRULES);

        DynamicRecommendationDTO dto1 = dynamicRecommendationMapper.toDynamicRecommendationDto(invest500DR);
        DynamicRecommendationDTO dto2 = dynamicRecommendationMapper.toDynamicRecommendationDto(topSavingDR);
        // List<DynamicRecommendation> listDr = new ArrayList<>(List.of(invest500DR, topSavingDR));

        // Установка id для DTO
        dto1.setId(1L);
        dto2.setId(2L);

        // Создайте список DTO
        List<DynamicRecommendationDTO> recommendationsList = Arrays.asList(dto1, dto2);
        ListDynamicRecommendationDTO responseDto = new ListDynamicRecommendationDTO();
        responseDto.setData(recommendationsList);

        // Настройте мок для recommendationService
        when(recommendationService.getAllDynamicRecommendations()).thenReturn(responseDto); // Здесь возвращается DTO

        LOGGER.info("Testing getAllDynamicRecommendations, expected : {}", recommendationsList);

        MvcResult result = mockMvc.perform(get("/recommendation/rule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Получите содержимое ответа

        MockHttpServletResponse response = mockMvc.perform(get("/recommendation/rule"))
                .andReturn()
                .getResponse();

        System.out.println("Response body: " + response.getContentAsString());

        // Выполните запрос и проверьте результат
        mockMvc.perform(get("/recommendation/rule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(dto1.getId().toString()))
                .andExpect(jsonPath("$.data[1].id").value(dto2.getId().toString()));
    }


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



    @Test
    public void testGetStatistics_Success() throws Exception {

        Statistic stat1 = new Statistic(1L, 10);
        Statistic stat2 = new Statistic(2L, 5);
        List<Statistic> statistics = Arrays.asList(stat1, stat2);

        // Настройка мока
        when(recommendationService.getStatistics()).thenReturn(statistics);

        // Выполнение запроса и ожидания результатов
        mockMvc.perform(get("/recommendation/rule/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Исправлено
                .andExpect(jsonPath("$", hasSize(2))) // Проверка размера списка
                .andExpect(jsonPath("$[0].recommendationId", is(1))) // Проверка первого элемента
                .andExpect(jsonPath("$[0].counter", is(10))) // Проверка счетчика первого элемента
                .andExpect(jsonPath("$[1].recommendationId", is(2))) // Проверка второго элемента
                .andExpect(jsonPath("$[1].counter", is(5))); // Проверка счетчика второго элемента
    }
}