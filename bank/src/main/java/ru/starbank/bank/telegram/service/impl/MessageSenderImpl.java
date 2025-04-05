package ru.starbank.bank.telegram.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.starbank.bank.telegram.service.MessageSender;

@Service
public class MessageSenderImpl implements MessageSender {

    private final Logger LOGGER = LoggerFactory.getLogger(MessageSenderImpl.class);

    private final TelegramBot telegramBot;

    @Autowired
    public MessageSenderImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage(long chatId, String message) {
        SendMessage messageText = new SendMessage(chatId, message);
        SendResponse response = telegramBot.execute(messageText);

        if (response.isOk()) {
            LOGGER.info("Message {} for chat {} has been sent", messageText, chatId);
        } else {
            LOGGER.error("Something went wrong {}", response.errorCode());
        }
    }

    public void sendWelcomeMessage(long chatId) {
        String welcomeMessage = "Привет! Добро пожаловать в нашего бота!";
        SendMessage message = new SendMessage(chatId, welcomeMessage);
        SendResponse response = telegramBot.execute(message);

        if (response.isOk()) {
            LOGGER.info("Welcome message for chat {} has been sent", chatId);
        } else {
            LOGGER.error("Something went wrong {}", response.errorCode());
        }
    }

    @Override
    public void sendErrorMessage(long chatId) {
        String welcomeMessage = "Пользователь не найден";
        SendMessage message = new SendMessage(chatId, welcomeMessage);
        SendResponse response = telegramBot.execute(message);

        if (response.isOk()) {
            LOGGER.info("Пользователь не найден {}", chatId);
        } else {
            LOGGER.error("Something went wrong {}", response.errorCode());
        }
    }
}
