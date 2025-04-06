package ru.starbank.bank.telegram.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.starbank.bank.telegram.service.MessageProcessorService;
import ru.starbank.bank.telegram.messageSender.MessageSender;

import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final String ERROR_MESSAGE = "Произошла ошибка при обработке вашего запроса." +
            "\nПожалуйста, попробуйте еще раз.";

    @Autowired
    private TelegramBot telegramBot;

    private final MessageProcessorService messageProcessorService;

    public TelegramBotUpdatesListener(MessageProcessorService messageProcessorService, MessageSender messageSender) {
        this.messageProcessorService = messageProcessorService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            long chatId = 0;

            try {
                if (update.message() != null) {
                    Message message = update.message();
                    String messageText = message.text();
                    Chat chat = message.chat();
                    chatId = chat.id();

                    if ("/start".equals(messageText)) {
                        messageProcessorService.sendWelcomeMessage(chatId);
                    } else if (messageText.startsWith("/recommend")) {
                        messageProcessorService.sendRecommendationMessage(chatId, messageText);
                    } else {
                        messageProcessorService.sendErrorMessage(chatId);
                    }
                }
            } catch (Exception e) {
                logger.error("Error processing update: {}", e.getMessage(), e);
                messageProcessorService.sendMessage(chatId, ERROR_MESSAGE);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
