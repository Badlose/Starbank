package ru.starbank.bank.telegram.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.service.RecommendationService;
import ru.starbank.bank.telegram.service.impl.MessageSenderImpl;
import ru.starbank.bank.telegram.service.impl.MessageServiceImpl;

import java.util.UUID;


@Component
public class MessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

    private final MessageServiceImpl messageService;
    private final MessageSender messageSender;



    @Autowired
    private TelegramBot telegramBot;

    public MessageProcessor(MessageServiceImpl messageService,
                            MessageSender messageSender) {
        this.messageService = messageService;
        this.messageSender = messageSender;
    }

    public String processMessage(String messageText) {
        String username = messageText.substring("/recommend ".length()).trim();
        UUID userId = messageService.getUserIdByUsername(username);
        String fullName = messageService.getFirstNameLastNameByUserName(username);
        UserRecommendationsDTO recommendationsDTO = messageService.getRecommendations(userId);

        return "Здравствуйте " + fullName + "\nНовые продукты для вас: " + recommendationsDTO;
    }

}
