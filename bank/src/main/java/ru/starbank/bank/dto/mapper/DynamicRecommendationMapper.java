package ru.starbank.bank.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RuleMapper.class})
public interface DynamicRecommendationMapper {

    DynamicRecommendation toDynamicRecommendation(DynamicRecommendationDTO recommendationDTO);

    @Mapping(source = "productId", target = "product_id")
    @Mapping(source = "text", target = "product_text")
    @Mapping(source = "ruleList", target = "rule")
    DynamicRecommendationDTO toDynamicRecommendationDTO(DynamicRecommendation recommendation);

}
