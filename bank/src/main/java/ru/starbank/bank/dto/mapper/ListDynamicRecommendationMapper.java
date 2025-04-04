package ru.starbank.bank.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.dto.ListDynamicRecommendationDTO;
import ru.starbank.bank.model.DynamicRecommendation;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ListDynamicRecommendationMapper {

    @Mapping(target = "data", source = "dynamicRecommendationList")
    ListDynamicRecommendationDTO toOtherDto(UUID userId, List<DynamicRecommendationDTO> dynamicRecommendationList);


    @Mapping(source = "product_name", target = "product_name")
    @Mapping(source = "product_id", target = "product_id")
    @Mapping(source = "product_text", target = "product_text")
    @Mapping(source = "rule", target = "rule")
    ListDynamicRecommendationDTO mapDynamicRecommendationDto(DynamicRecommendationDTO dynamicRecommendationDto);

}
