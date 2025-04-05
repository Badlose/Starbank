package ru.starbank.bank.telegram;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.repository.TransactionsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
    private final TransactionsRepository transactionsRepository;
    private final MessageSender messageSender; // Внедряем MessageSender


    /** Хранение команд
     *
     */
    private Map<String, Command> commands = new HashMap<>();

    @Autowired
    public MessageProcessor(TransactionsRepository transactionsRepository, MessageSender messageSender) {
        this.transactionsRepository = transactionsRepository;
        this.messageSender = messageSender; // Инициализируем MessageSender
    }

    /** Инициализация команд
     *
     */
    @PostConstruct
    public void init() {
        commands.put("/start", this::sendWelcomeMessage);
        commands.put("/recommend", this::processRecommendation);
    }

    public void processMessage(String messageText, long chatId) {
        logger.info("Получено сообщение от пользователя {}: {}", chatId, messageText);

        /** Проверяем, если команда известна
         *
         */
        Command command = commands.get(messageText.split(" ")[0]);
        if (command != null) {
            /** Передаем текст сообщения для обработки
             *
             */
            command.execute(chatId, messageText);
        } else {
            messageSender.sendMessage(chatId, "Неизвестная команда. Пожалуйста, используйте /start или /recommend <Имя Фамилия>.");
        }
    }

    private void sendWelcomeMessage(long chatId, String messageText) {
        messageSender.sendWelcomeMessage(chatId);
    }

    private void processRecommendation(long chatId, String messageText) {
        /** Проверяем, начинается ли сообщение с команды /recommend
         *
         */
        if (messageText.startsWith("/recommend ")) {
            // Извлекаем имя и фамилию из сообщения
            String username = messageText.substring("/recommend ".length()).trim();

            /** Проверяем, что введено имя и фамилия
             *
             */
            if (username.isEmpty()) {
                messageSender.sendMessage(chatId, "Пожалуйста, укажите ваше имя и фамилию после команды /recommend.");
                return;
            }

            String[] nameParts = username.split(" ");
            if (nameParts.length < 2 || nameParts[0].isEmpty() || nameParts[1].isEmpty()) {
                messageSender.sendMessage(chatId, "Пожалуйста, укажите ваше имя и фамилию через пробел.");
                return;
            }

            String firstName = nameParts[0];
            String lastName = nameParts[1];

            /** Получаем рекомендации для пользователя
             *
             */
            Optional<List<DynamicRecommendation>> optionalRecommendations = transactionsRepository.getRecommendationsByFirstNameAndLastName(firstName, lastName);

            if (optionalRecommendations.isPresent()) {
                List<DynamicRecommendation> recommendations = optionalRecommendations.get();
                if (recommendations.isEmpty()) {
                    messageSender.sendMessage(chatId, "У вас нет доступных рекомендаций.");
                    return;
                }

                /** Формируем ответ с рекомендациями
                 *
                 */
                StringBuilder response = new StringBuilder("Ваши рекомендации:\n");
                for (DynamicRecommendation recommendation : recommendations) {
                    response.append("- ").append(recommendation.getText()).append("\n");
                }
                messageSender.sendMessage(chatId, response.toString());
            } else {
                messageSender.sendMessage(chatId, "Не удалось получить рекомендации. Попробуйте позже.");
            }
        } else {
            messageSender.sendMessage(chatId, "Неизвестная команда. Пожалуйста, используйте /recommend Имя Фамилия для получения рекомендаций.");
        }
    }

    @FunctionalInterface
    public interface Command {
        void execute(long chatId, String messageText);
    }
}






