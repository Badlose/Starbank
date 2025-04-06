package ru.starbank.bank.telegram.service;

import ru.starbank.bank.dto.TelegramRecommendationDTO;

import java.util.UUID;

public interface MessageService {

    String getFirstNameLastNameByUserName(String userName);

    UUID getUserIdByUsername(String userName);

    TelegramRecommendationDTO getRecommendations(UUID userId);

}
