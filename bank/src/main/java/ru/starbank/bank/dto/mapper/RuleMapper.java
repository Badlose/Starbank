//package ru.starbank.bank.dto.mapper;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import ru.starbank.bank.dto.RuleDTO;
//import ru.starbank.bank.model.Rule;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface RuleMapper {
//
//    @Mapping(source = "query", target = "query")
//    @Mapping(source = "arguments", target = "arguments")
//    @Mapping(source = "negate", target = "negate")
//    RuleDTO ruleToDTO(Rule rule);
//
//
//    default String argumentsListToString(List<String> arguments) {
//        if (arguments == null || arguments.isEmpty()) {
//            return "";
//        }
//        return String.join(", ", arguments);
//    }
//}
