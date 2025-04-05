package ru.starbank.bank.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.starbank.bank.repository.RecommendationsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class MessageProcessor {
    private final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
    private final RecommendationsRepository recommendationsRepository;

    public MessageProcessor( RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }


    public void processMessage(String messageText, long chatId) {

    }
}
