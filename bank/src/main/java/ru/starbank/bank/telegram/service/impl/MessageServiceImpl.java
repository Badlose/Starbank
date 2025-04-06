package ru.starbank.bank.telegram.service.impl;

import org.springframework.stereotype.Service;
import ru.starbank.bank.dto.TelegramRecommendationDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.dto.mapper.TelegramRecommendationsMapper;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.RecommendationService;
import ru.starbank.bank.telegram.service.MessageService;

import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {
    private final TransactionsRepository transactionsRepository;
    private final RecommendationService recommendationService;
    private final TelegramRecommendationsMapper telegramRecommendationsMapper;

    public MessageServiceImpl(TransactionsRepository transactionsRepository,
                              RecommendationService recommendationService, TelegramRecommendationsMapper telegramRecommendationsMapper) {
        this.transactionsRepository = transactionsRepository;
        this.recommendationService = recommendationService;
        this.telegramRecommendationsMapper = telegramRecommendationsMapper;
    }

    public UUID getUserIdByUsername(String userName) {
        return transactionsRepository.getUserIdByUserName(userName);
    }

    public String getFirstNameLastNameByUserName(String userName) {

        return transactionsRepository.getFirstNameLastNameByUserName(userName);
    }

    public TelegramRecommendationDTO getRecommendations(UUID userId) {
        UserRecommendationsDTO userRecommendationsDTO = recommendationService.getRecommendation(userId);

        return telegramRecommendationsMapper.userDTOListToTelegramRecommendationDTO(
                userRecommendationsDTO.getRecommendations());
    }

}
