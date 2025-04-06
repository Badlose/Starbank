package ru.starbank.bank.telegram.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.starbank.bank.dto.TelegramRecommendationDTO;
import ru.starbank.bank.telegram.service.MessageProcessorService;
import ru.starbank.bank.telegram.messageSender.MessageSender;

import java.util.UUID;

@Service
public class MessageProcessorServiceImpl implements MessageProcessorService {

    private final Logger logger = LoggerFactory.getLogger(MessageProcessorServiceImpl.class);
    private final String WELCOME_MESSAGE = "Привет! Добро пожаловать в нашего бота!";
    private final String ERROR_MESSAGE = "Введена некорректная команда. Вот пример доступных: /recommend + username";
    private final String USER_NOT_FOUND_MESSAGE = "Пользователь не найден";
    private final String RECOMMENDATIONS_NOT_FOUND_MESSAGE = "Рекомендации не найдены";
    private final MessageServiceImpl messageService;
    private final MessageSender messageSender;


    @Autowired
    public MessageProcessorServiceImpl(MessageServiceImpl messageService, MessageSender messageSender) {
        this.messageService = messageService;
        this.messageSender = messageSender;
    }

    @Override
    public void sendMessage(long chatId, String message) {
        messageSender.sendMessage(chatId, message);
    }

    public void sendWelcomeMessage(long chatId) {
        messageSender.sendMessage(chatId, WELCOME_MESSAGE);
    }

    //как разделить логику?

    public void sendRecommendationMessage(long chatId, String messageText) {

        String username = messageText.substring("/recommend".length()).trim(); //не то?

        String fullName = "";
        TelegramRecommendationDTO recommendationsDTO = null;
        UUID userId = null;

        try {
            userId = messageService.getUserIdByUsername(username);
        } catch (Exception e) {
            logger.error("Error when getting the userId, update: {}", e.getMessage(), e);
            messageSender.sendMessage(chatId, USER_NOT_FOUND_MESSAGE);
            return; //ох даааа
        }

        try {
            fullName = messageService.getFirstNameLastNameByUserName(username);
        } catch (Exception e) {
            logger.error("Error when getting the fullName, update: {}", e.getMessage(), e);
            messageSender.sendMessage(chatId, USER_NOT_FOUND_MESSAGE);
            return;
        }

        try {
            recommendationsDTO = messageService.getRecommendations(userId);
        } catch (Exception e) {
            logger.error("Error when getting the recommendationDTO, update: {}", e.getMessage(), e);
            messageSender.sendMessage(chatId, RECOMMENDATIONS_NOT_FOUND_MESSAGE);
            return;
        }

        String recommendationMessage = "Здравствуйте " + fullName + "\nНовые продукты для вас: " + "\n" + recommendationsDTO;

        messageSender.sendMessage(chatId, recommendationMessage);
    }

    @Override
    public void sendErrorMessage(long chatId) {
        messageSender.sendMessage(chatId, ERROR_MESSAGE);
    }

}
