package ru.starbank.bank.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.starbank.bank.dto.RuleDTO;
import ru.starbank.bank.model.Rule;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RuleMapper {

    @Mapping(source = "arguments", target = "arguments")
    RuleDTO ruleToDTO(Rule rule);

    Rule toRule(RuleDTO ruleDTO);

    default String argumentsListToString(List<String> arguments) {
        if (arguments == null || arguments.isEmpty()) {
            return "";
        }
        return String.join(", ", arguments);
    }
}
