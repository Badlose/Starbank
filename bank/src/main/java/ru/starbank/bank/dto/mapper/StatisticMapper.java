package ru.starbank.bank.dto.mapper;

import org.mapstruct.Mapper;
import ru.starbank.bank.dto.StatisticsDTO;
import ru.starbank.bank.model.Statistic;

@Mapper(componentModel = "spring")
public interface StatisticMapper {

    StatisticsDTO toStatisticDto(Statistic statistic);
}
