package ru.starbank.bank.telegram.service;

import ru.starbank.bank.dto.TelegramRecommendationDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;

import java.util.UUID;

public interface MessageService {

    String getFirstNameLastNameByUserName(String userName);

    UUID getUserIdByUsername(String userName);

    TelegramRecommendationDTO getRecommendations(UUID userId);

}
