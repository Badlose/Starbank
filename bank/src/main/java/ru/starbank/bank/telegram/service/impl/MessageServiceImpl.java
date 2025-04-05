package ru.starbank.bank.telegram.service.impl;

import org.springframework.stereotype.Service;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.telegram.service.MessageService;

import java.util.UUID;
@Service
public class MessageServiceImpl implements MessageService {
    private final TransactionsRepository transactionsRepository;

    public MessageServiceImpl(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }


    String extractUsername(String messageText) {
        // Убедимся, что сообщение начинается с /recommend
        if (messageText.startsWith("/recommend")) {
            // Разделяем строку по пробелам
            String[] parts = messageText.split(" ");

            // Проверяем, есть ли дополнительная часть после команды
            if (parts.length > 1) {
                // Возвращаем имя пользователя, убирая лишние символы (например, пробелы)
                return parts[1].trim();
            }
        }
        // Если команда не распознана или имя пользователя отсутствует, возвращаем null
        return null;
    }

    private UUID getUserIdByUsername(String userName) {
      return   transactionsRepository.getUserIdByUserName(userName);

    }

    private String getFirstNameLastNameByUserName(String userName) {
        String fullName = transactionsRepository.getFirstNameLastNameByUserName(userName);

        return fullName;
    }

}
