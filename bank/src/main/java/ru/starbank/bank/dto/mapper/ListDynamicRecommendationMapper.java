package ru.starbank.bank.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.dto.ListDynamicRecommendationDTO;
import ru.starbank.bank.dto.RuleDTO;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ListDynamicRecommendationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "product_name")
    @Mapping(source = "productId", target = "product_id")
    @Mapping(source = "text", target = "product_text")
    @Mapping(source = "ruleList", target = "rule")
    DynamicRecommendationDTO toDynamicRecommendationResponseDto(DynamicRecommendation dynamicRecommendation);

    @Mapping(source = "query", target = "query")
    @Mapping(source = "arguments", target = "arguments")
    @Mapping(source = "negate", target = "negate")
    RuleDTO toRuleDto(Rule rule);

    List<RuleDTO> mapRuleListToRuleDtoList(List<Rule> ruleList);

    default ListDynamicRecommendationDTO toRecommendationListResponseDto(List<DynamicRecommendation> dynamicRecommendations) {
        ListDynamicRecommendationDTO response = new ListDynamicRecommendationDTO();
        List<DynamicRecommendationDTO> data = dynamicRecommendations.stream()
                .map(this::toDynamicRecommendationResponseDto)
                .toList();
        response.setData(data);
        return response;
    }

}