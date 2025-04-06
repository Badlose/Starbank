package ru.starbank.bank.telegram.messageSender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starbank.bank.telegram.service.impl.MessageProcessorServiceImpl;
import ru.starbank.bank.telegram.service.impl.MessageServiceImpl;


@Component
public class MessageSender {

    private final Logger LOGGER = LoggerFactory.getLogger(MessageProcessorServiceImpl.class);

    private final TelegramBot telegramBot;

    @Autowired
    public MessageSender(TelegramBot telegramBot, MessageServiceImpl messageService) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage(long chatId, String message) {
        SendMessage messageText = new SendMessage(chatId, message);
        SendResponse response = telegramBot.execute(messageText);

        if (response.isOk()) {
            LOGGER.info("Message {} for chat {} has been sent", messageText, chatId);
        } else {
            LOGGER.error("Something went wrong {}", response.errorCode());
        }
    }

}
