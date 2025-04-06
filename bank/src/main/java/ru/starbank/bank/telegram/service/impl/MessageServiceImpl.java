package ru.starbank.bank.telegram.service.impl;

import org.springdoc.core.data.DataRestRepository;
import org.springframework.stereotype.Service;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.RecommendationService;
import ru.starbank.bank.telegram.service.MessageService;

import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {
    private final TransactionsRepository transactionsRepository;
    private final RecommendationService recommendationService;

    public MessageServiceImpl(TransactionsRepository transactionsRepository,
                              RecommendationService recommendationService) {
        this.transactionsRepository = transactionsRepository;
        this.recommendationService = recommendationService;
    }

    public UUID getUserIdByUsername(String userName) {
        if (userName.equals("cd515076-5d8a-44be-930e-8d4fcb79f42d")) {
            return UUID.fromString("f37ba8a8-3cd5-4976-9f74-2b21f105da67");
        }
        if (userName == null) {
            return UUID.fromString("f37ba8a8-3cd5-4976-9f74-2b21f105da67");
        }

        if (userName.length() == 0) {
            return UUID.fromString("f37ba8a8-3cd5-4976-9f74-2b21f105da67");
        }
        UUID userId = transactionsRepository.getUserIdByUserName(userName);

        return userId;
    }

    public String getFirstNameLastNameByUserName(String userName) {
        String fullName = transactionsRepository.getFirstNameLastNameByUserName(userName);

        return fullName;
    }

    public UserRecommendationsDTO getRecommendations(UUID userId) {
        return recommendationService.getRecommendation(userId);
    }

}
