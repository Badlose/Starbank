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
import ru.starbank.bank.telegram.service.MessageProcessor;
import ru.starbank.bank.telegram.service.MessageSender;

import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    private final MessageSender messageSender;

    private final MessageProcessor messageProcessor;

    public TelegramBotUpdatesListener(MessageSender messageSender, MessageProcessor messageProcessor) {
        this.messageSender = messageSender;
        this.messageProcessor = messageProcessor;
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
                        messageSender.sendWelcomeMessage(chatId);
                    } else if (messageText.startsWith("/recommend")) {
                        String responseMessage = messageProcessor.processMessage(messageText);
                        messageSender.sendMessage(chatId, responseMessage);
                    } else {
                        messageSender.sendErrorMessage(chatId);
                    }
                }
            } catch (Exception e) {
                logger.error("Error processing update: {}", e.getMessage(), e);
                messageSender.sendMessage(chatId, "Произошла ошибка при обработке вашего запроса." +
                        "\nПожалуйста, попробуйте еще раз.");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
