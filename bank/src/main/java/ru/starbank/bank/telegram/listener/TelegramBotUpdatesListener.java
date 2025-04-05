package ru.starbank.bank.telegram.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Chat;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.starbank.bank.service.Impl.RecommendationServiceImpl;
import ru.starbank.bank.telegram.service.MessageProcessor;
import ru.starbank.bank.telegram.service.MessageSender; // Импортируйте новый класс

import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private RecommendationServiceImpl recommendationService;

    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private MessageSender messageSender; // Внедрите новый класс

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            if (update.message() != null) {
                Message message = update.message();
                String messageText = message.text();
                Chat chat = message.chat();
                long chatId = chat.id();

                if ("/start".equals(messageText)) {
                    messageSender.sendWelcomeMessage(chatId); // Используйте MessageService
                } else {
                    messageProcessor.processMessage(messageText, chatId);

                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
