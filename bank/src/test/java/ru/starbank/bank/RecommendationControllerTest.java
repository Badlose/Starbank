package ru.starbank.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.starbank.bank.Controller.RecommendationController;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.RecommendationService;

import java.util.Optional;
import java.util.UUID;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RecommendationControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    private MockMvc mockMvc;

    private UUID userId;
    private Recommendation recommendation;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
        userId = UUID.randomUUID();
        recommendation = new Recommendation();
        recommendation.setText("Sample Recommendation");
    }

    @Test
    public void testGetRecommendation_ReturnsOkWithEmptyResponse() throws Exception {
        UUID userId = UUID.randomUUID();

        when(recommendationService.getRecommendation(userId)).thenReturn(Optional.empty());


        mockMvc.perform(get("/recommendation/user_id/{user_id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("null"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    public void testGetRecommendation_ReturnsRecommendation() throws Exception {
        when(recommendationService.getRecommendation(userId)).thenReturn(Optional.of(recommendation));

        mockMvc.perform(get("/recommendation/user_id/{user_id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value(recommendation.getText()));
    }
}




