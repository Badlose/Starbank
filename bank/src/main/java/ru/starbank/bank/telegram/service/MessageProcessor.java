package ru.starbank.bank.telegram.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.service.RecommendationService;
import ru.starbank.bank.telegram.service.impl.MessageServiceImpl;

import java.util.UUID;


@Component
public class MessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);



    private final MessageSender messageSender;
    private final RecommendationService recommendationService;
    private final MessageServiceImpl messageService;



    @Autowired
    private TelegramBot telegramBot;

    public MessageProcessor(RecommendationsRepository recommendationsRepository, MessageSender messageSender, RecommendationService recommendationService, MessageProcessor messageProcessor, MessageServiceImpl messageService) {
        this.recommendationService = recommendationService;
        this.messageSender = messageSender;

        this.messageService = messageService;
    }


    public void processMessage(String messageText, long chatId) {





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


    }

