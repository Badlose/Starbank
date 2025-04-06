package ru.starbank.bank.dto.mapper;

import org.mapstruct.Mapper;
import ru.starbank.bank.dto.TelegramRecommendationDTO;
import ru.starbank.bank.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TelegramRecommendationsMapper {

    default TelegramRecommendationDTO userDTOListToTelegramRecommendationDTO(List<UserDTO> userDTOList) {
        TelegramRecommendationDTO telegramRecommendationDTO = new TelegramRecommendationDTO();
        if (userDTOList == null || userDTOList.isEmpty()) {
            telegramRecommendationDTO.setProductText("");
            return telegramRecommendationDTO;
        }

        String concatenatedText = userDTOList.stream()
                .map(UserDTO::getProduct_text)
                .collect(Collectors.joining(", \n"));

        telegramRecommendationDTO.setProductText(concatenatedText);
        return telegramRecommendationDTO;
    }
}

