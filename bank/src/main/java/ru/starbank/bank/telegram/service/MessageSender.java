package ru.starbank.bank.telegram.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {


    private final TelegramBot telegramBot;

    @Autowired
    public MessageSender(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendWelcomeMessage(long chatId) {
        String welcomeMessage = "Привет! Добро пожаловать в нашего бота!";
        SendMessage message = new SendMessage(chatId, welcomeMessage);
        telegramBot.execute(message);
    }


    public void sendMessage(long chatId, String s) {

    }
}

