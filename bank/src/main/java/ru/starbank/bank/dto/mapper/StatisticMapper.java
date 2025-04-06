package ru.starbank.bank.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.starbank.bank.dto.ListStatisticDTO;
import ru.starbank.bank.dto.StatisticDTO;
import ru.starbank.bank.model.Statistic;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatisticMapper {

    @Mapping(source = "recommendationId", target = "rule_id")
    @Mapping(source = "counter", target = "count")
    StatisticDTO toStatisticDto(Statistic statistic);

    List<StatisticDTO> mapStatisticListToStatisticDtoList(List<Statistic> statisticList);

    default ListStatisticDTO toStatisticListResponseDto(List<Statistic> statisticList) {
        ListStatisticDTO response = new ListStatisticDTO();
        response.setStats(mapStatisticListToStatisticDtoList(statisticList));
        return response;
    }

}