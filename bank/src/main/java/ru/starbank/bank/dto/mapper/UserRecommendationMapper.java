package ru.starbank.bank.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.starbank.bank.dto.UserDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.model.DynamicRecommendation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserRecommendationMapper {

    // Метод для преобразования DynamicRecommendation в RecommendationDto
    @Mapping(source = "name", target = "name")
    @Mapping(source = "productId", target = "id")
    @Mapping(source = "text", target = "text")
    UserDTO dynamicRecommendationToRecommendationDto(DynamicRecommendation dynamicRecommendation);


    // Метод для создания RecommendationResponseDto из userId и списка DynamicRecommendation
    default UserRecommendationsDTO toRecommendationResponseDto(UUID userId, List<DynamicRecommendation> dynamicRecommendations) {
        List<UserDTO> recommendationDTOS = dynamicRecommendations.stream()
                .map(this::dynamicRecommendationToRecommendationDto)
                .collect(Collectors.toList());

        UserRecommendationsDTO response = new UserRecommendationsDTO();
        response.setUserId(userId);
        response.setRecommendations(recommendationDTOS);
        return response;
    }
}
