package ru.starbank.bank.telegram.service;

import com.pengrad.telegrambot.TelegramBot;
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


    private final MessageSenderImpl messageSenderImpl;
    private final RecommendationService recommendationService;
    private final MessageServiceImpl messageService;
    private final MessageSender messageSender;



    @Autowired
    private TelegramBot telegramBot;

    public MessageProcessor(RecommendationsRepository recommendationsRepository, MessageSenderImpl messageSenderImpl, RecommendationService recommendationService, MessageProcessor messageProcessor, MessageServiceImpl messageService, MessageSender messageSender) {
        this.recommendationService = recommendationService;
        this.messageSenderImpl = messageSenderImpl;
        this.messageService = messageService;
        this.messageSender = messageSender;
    }

    public void processMessage(String messageText, long chatId) {
        String username = messageText.substring("/recommend ".length()).trim();
        UUID userId = messageService.getUserIdByUsername(username);
        String fullName = messageService.getFirstNameLastNameByUserName(username);
        UserRecommendationsDTO recommendationsDTO = recommendationService.getRecommendation(userId);
        String preparedRecommendations = "Новые продукты для вас: " + recommendationsDTO;

        messageSender.sendMessage(chatId, preparedRecommendations);

    }



//        String userName = messageSender.extractUsername(messageText);
//
//
//        List<DynamicRecommendation> recommendations = recommendationsRepository.findAll();
//
//        // Проверяем, есть ли рекомендации
//        if (recommendations.isEmpty()) {
//            // Если рекомендации отсутствуют, отправляем сообщение об этом
//            sendConfirmationMessage(chatId, "К сожалению, в данный момент нет доступных рекомендаций.");
//        } else {
//            // Если рекомендации есть, обрабатываем их
//            StringBuilder responseMessage = new StringBuilder("Вот ваши рекомендации:\n");
//            for (DynamicRecommendation recommendation : recommendations) {
//                responseMessage.append("- ").append(recommendation.getProduct_id()).append("\n");
//            }
//            sendConfirmationMessage(chatId, responseMessage.toString());
//        }


    }




