package ru.starbank.bank.telegram.service;

public interface MessageProcessorService {

    void sendMessage(long chatId, String message);

    void sendWelcomeMessage(long chatId);

    void sendErrorMessage(long chatId);

    void sendRecommendationMessage(long chatId, String messageText);

}

