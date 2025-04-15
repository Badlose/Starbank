package ru.starbank.bank.telegram.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.starbank.bank.dto.TelegramRecommendationDTO;
import ru.starbank.bank.dto.UserDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.dto.mapper.TelegramRecommendationsMapper;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.RecommendationService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    @Mock
    private TransactionsRepository transactionsRepository;

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private TelegramRecommendationsMapper telegramRecommendationsMapper;

    @InjectMocks
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserIdByUsername_shouldReturnUserId() {
        String userName = "testUser";
        UUID expectedId = UUID.randomUUID();
        when(transactionsRepository.getUserIdByUserName(userName)).thenReturn(expectedId);

        UUID actualId = messageService.getUserIdByUsername(userName);

        assertEquals(expectedId, actualId);
        verify(transactionsRepository).getUserIdByUserName(userName);
    }

    @Test
    void getFirstNameLastNameByUserName_shouldReturnFullName() {
        String userName = "testUser";
        String expectedName = "Ivan Ivanov";
        when(transactionsRepository.getFirstNameLastNameByUserName(userName)).thenReturn(expectedName);

        String actualName = messageService.getFirstNameLastNameByUserName(userName);

        assertEquals(expectedName, actualName);
        verify(transactionsRepository).getFirstNameLastNameByUserName(userName);
    }

    @Test
    void getRecommendations_shouldReturnTelegramRecommendationDTO() {
        UUID userId = UUID.randomUUID();
        UserRecommendationsDTO userRecommendationsDTO = mock(UserRecommendationsDTO.class);
        List<UserDTO> recommendations = List.of(mock(UserDTO.class));
        TelegramRecommendationDTO expectedTelegramDTO = mock(TelegramRecommendationDTO.class);

        when(recommendationService.getRecommendation(userId)).thenReturn(userRecommendationsDTO);
        when(userRecommendationsDTO.getRecommendations()).thenReturn(recommendations);
        when(telegramRecommendationsMapper.userDTOListToTelegramRecommendationDTO(recommendations))
                .thenReturn(expectedTelegramDTO);

        TelegramRecommendationDTO actualTelegramDTO = messageService.getRecommendations(userId);

        assertEquals(expectedTelegramDTO, actualTelegramDTO);
        verify(recommendationService).getRecommendation(userId);
        verify(telegramRecommendationsMapper).userDTOListToTelegramRecommendationDTO(recommendations);
    }
}