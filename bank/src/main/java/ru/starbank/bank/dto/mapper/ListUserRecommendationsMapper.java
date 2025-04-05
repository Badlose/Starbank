//package ru.starbank.bank.dto.mapper;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import ru.starbank.bank.dto.UserDTO;
//import ru.starbank.bank.dto.UserRecommendationsDTO;
//
//import java.util.List;
//import java.util.UUID;
//
//@Mapper(componentModel = "spring", uses = {UserRecommendationMapper.class})
//public interface ListUserRecommendationsMapper {
//
//    @Mapping(source = "userRecommendations", target = "recommendations")
//    UserRecommendationsDTO toUserRecommendationsDTO(UUID userId, List<UserDTO> userDTOS);
//}
