package ru.starbank.bank.telegram.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.starbank.bank.dto.TelegramRecommendationDTO;
import ru.starbank.bank.telegram.messageSender.MessageSender;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageProcessorServiceImplTest {

    @Mock
    private MessageServiceImpl messageService;

    @Mock
    private MessageSender messageSender;

    @InjectMocks
    private MessageProcessorServiceImpl messageProcessorService;

    private final long chatId = 12345L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendWelcomeMessage_shouldSendWelcomeText() {
        messageProcessorService.sendWelcomeMessage(chatId);

        verify(messageSender).sendMessage(chatId, "Привет! Добро пожаловать в нашего бота!");
    }

    @Test
    void sendErrorMessage_shouldSendErrorText() {
        messageProcessorService.sendErrorMessage(chatId);

        verify(messageSender).sendMessage(chatId, "Введена некорректная команда. Вот пример доступных: /recommend + username");
    }

    @Test
    void sendRecommendationMessage_successfulFlow() {
        String username = "testuser";
        String messageText = "/recommend " + username;
        UUID userId = UUID.randomUUID();
        String fullName = "Иван Иванов";
        TelegramRecommendationDTO dto = new TelegramRecommendationDTO();
        dto.setProductText("Product1, Product2");

        when(messageService.getUserIdByUsername(username)).thenReturn(userId);
        when(messageService.getFirstNameLastNameByUserName(username)).thenReturn(fullName);
        when(messageService.getRecommendations(userId)).thenReturn(dto);

        messageProcessorService.sendRecommendationMessage(chatId, messageText);

        String expectedMessage = "Здравствуйте " + fullName + "\nНовые продукты для вас: \n" + dto;
        verify(messageSender).sendMessage(chatId, expectedMessage);
    }

    @Test
    void sendRecommendationMessage_userNotFound() {
        String username = "nouser";
        String messageText = "/recommend " + username;

        when(messageService.getUserIdByUsername(username)).thenThrow(new RuntimeException("User not found"));

        messageProcessorService.sendRecommendationMessage(chatId, messageText);

        verify(messageSender).sendMessage(chatId, "Пользователь не найден");

        verify(messageService).getUserIdByUsername(username);
        verifyNoMoreInteractions(messageService);
    }

    @Test
    void sendRecommendationMessage_fullNameNotFound() {
        String username = "testuser";
        String messageText = "/recommend " + username;
        UUID userId = UUID.randomUUID();

        when(messageService.getUserIdByUsername(username)).thenReturn(userId);
        when(messageService.getFirstNameLastNameByUserName(username)).thenThrow(new RuntimeException("No name"));

        messageProcessorService.sendRecommendationMessage(chatId, messageText);

        verify(messageSender).sendMessage(chatId, "Пользователь не найден");
    }

    @Test
    void sendRecommendationMessage_recommendationsNotFound() {
        String username = "testuser";
        String messageText = "/recommend " + username;
        UUID userId = UUID.randomUUID();
        String fullName = "Иван Иванов";

        when(messageService.getUserIdByUsername(username)).thenReturn(userId);
        when(messageService.getFirstNameLastNameByUserName(username)).thenReturn(fullName);
        when(messageService.getRecommendations(userId)).thenThrow(new RuntimeException("No recommendations"));

        messageProcessorService.sendRecommendationMessage(chatId, messageText);

        verify(messageSender).sendMessage(chatId, "Рекомендации не найдены");
    }
}