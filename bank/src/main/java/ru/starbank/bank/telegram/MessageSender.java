package ru.starbank.bank.telegram;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    private final TelegramBot telegramBot;

    @Autowired
    public MessageSender(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }

    public void sendWelcomeMessage(long chatId) {
        String welcomeMessage = "Привет! Добро пожаловать в нашего бота! Используйте команду /recommend <Имя Фамилия>, чтобы получить рекомендации.";
        sendMessage(chatId, welcomeMessage);
    }
}

