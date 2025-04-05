package ru.starbank.bank.telegram.service;

public interface MessageSender {

    void sendMessage(long chatId, String message);

    void sendWelcomeMessage(long chatId);
}

