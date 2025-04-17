package ru.starbank.bank.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.starbank.bank.dto.DynamicRecommendationDTO;
import ru.starbank.bank.dto.RuleDTO;
import ru.starbank.bank.dto.UserDTO;
import ru.starbank.bank.dto.UserRecommendationsDTO;
import ru.starbank.bank.dto.mapper.DynamicRecommendationMapper;
import ru.starbank.bank.dto.mapper.ListDynamicRecommendationMapper;
import ru.starbank.bank.dto.mapper.UserRecommendationMapper;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.model.Statistic;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.repository.RulesRepository;
import ru.starbank.bank.repository.StatisticRepository;
import ru.starbank.bank.service.CheckRecommendationByUserIdService;
import ru.starbank.bank.service.CheckRecommendationService;
import ru.starbank.bank.service.CheckRuleService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceImplUnitTest {
    @Mock
    private CheckRecommendationByUserIdService checkerService;
    @Mock
    private CheckRuleService ruleService;
    @Mock
    private CheckRecommendationServiceImpl checkRecommendation;
    @Mock
    private RecommendationsRepository recommendationsRepository;
    @Mock
    private RulesRepository rulesRepository;
    @Mock
    private StatisticRepository statisticRepository;
    @Mock
    private DynamicRecommendationMapper recommendationMapper;
    @Mock
    private UserRecommendationMapper userRecommendationMapper;
    @Mock
    private ListDynamicRecommendationMapper listDynamicRecommendationMapper;
    @InjectMocks
    private RecommendationServiceImpl service;

    private static Statistic statistic = new Statistic(1L, 0);

    private static final UUID userIdInvest500 = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
    private static final UUID INVEST500ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private static final String INVEST500NAME = "Invest500";
    private static final String INVEST500TEXT = "\nОткройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! " +
            "Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. " +
            "Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private static final Rule rule1 = new Rule("USER_OF", List.of("DEBIT"), true);
    private static final Rule rule2 = new Rule("USER_OF", List.of("INVEST"), false);
    private static final Rule rule3 = new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000"), true);

    private static final List<Rule> INVEST500RULES = new ArrayList<>(List.of(
            rule1,
            rule2,
            rule3
    ));

    private static final DynamicRecommendation invest500DR = new DynamicRecommendation(
            INVEST500NAME,
            INVEST500ID,
            INVEST500TEXT,
            INVEST500RULES);
    private static DynamicRecommendationDTO dynamicRecommendationDTO = new DynamicRecommendationDTO();
    private static RuleDTO ruleDTO1 = new RuleDTO();
    private static RuleDTO ruleDTO2 = new RuleDTO();
    private static RuleDTO ruleDTO3 = new RuleDTO();
    private static final List<DynamicRecommendation> listDR = new ArrayList<>(List.of(invest500DR));

    private UserDTO userDTO = new UserDTO();

    private UserRecommendationsDTO userRecommendationsDTO = new UserRecommendationsDTO();

    @Test
    public void shouldGetAllRecommendationsByUserId() {
        userDTO.setProduct_name(invest500DR.getName());
        userDTO.setProduct_id(invest500DR.getProductId());
        userDTO.setProduct_text(invest500DR.getText());

        when(recommendationsRepository.findAll()).thenReturn(listDR);
        when(checkerService.checkDynamicRecommendation(userIdInvest500, invest500DR)).thenReturn(true);
        when(statisticRepository.findByRecommendationId(invest500DR.getId())).thenReturn(statistic);

        UserRecommendationsDTO expectedDTO = new UserRecommendationsDTO();
        expectedDTO.setUserId(userIdInvest500);
        List<UserDTO> userDTOS = new ArrayList<>(List.of(userDTO));
        expectedDTO.setRecommendations(userDTOS);

        when(userRecommendationMapper.toRecommendationResponseDto(userIdInvest500, List.of(invest500DR)))
                .thenReturn(expectedDTO);

        UserRecommendationsDTO actualDTO = service.getRecommendation(userIdInvest500);

        assertEquals(expectedDTO, actualDTO);
        verify(recommendationsRepository, times(1)).findAll();
        verify(statisticRepository, times(1)).findByRecommendationId(invest500DR.getId());
        verify(checkerService, times(1)).checkDynamicRecommendation(userIdInvest500, invest500DR);
        verify(userRecommendationMapper, times(1)).
                toRecommendationResponseDto(userIdInvest500, List.of(invest500DR));
    }

    @Test
    public void shouldCreateNewDynamicRecommendation() {
        rule1.setId(1L);
        rule2.setId(2L);
        rule3.setId(3L);
        ruleDTO1.setQuery(rule1.getQuery());
        ruleDTO2.setQuery(rule2.getQuery());
        ruleDTO3.setQuery(rule3.getQuery());
        ruleDTO1.setArguments(rule1.getArguments());
        ruleDTO2.setArguments(rule2.getArguments());
        ruleDTO3.setArguments(rule3.getArguments());
        ruleDTO1.setNegate(rule1.isNegate());
        ruleDTO2.setNegate(rule2.isNegate());
        ruleDTO3.setNegate(rule3.isNegate());
        invest500DR.setId(1L);

        DynamicRecommendationDTO expectedDynamicRecommendationDTO = new DynamicRecommendationDTO();
        expectedDynamicRecommendationDTO.setId(1L);
        expectedDynamicRecommendationDTO.setProduct_id(invest500DR.getProductId());
        expectedDynamicRecommendationDTO.setProduct_name(invest500DR.getName());
        expectedDynamicRecommendationDTO.setProduct_text(invest500DR.getText());
        expectedDynamicRecommendationDTO.setRule(List.of(ruleDTO1, ruleDTO2, ruleDTO3));

        when(checkRecommendation.checkRecommendationCorrect(invest500DR)).thenReturn(true);
        when(recommendationsRepository.save(invest500DR)).thenReturn(invest500DR);
        when(statisticRepository.save(any(Statistic.class))).thenReturn(statistic);
        for (Rule rule : invest500DR.getRuleList()) {
            when(rulesRepository.save(rule)).thenReturn(rule);
        }

        when(recommendationMapper.toDynamicRecommendationDto(invest500DR)).thenReturn(expectedDynamicRecommendationDTO);

        DynamicRecommendationDTO actualDynamicRecommendationDTO = service.createNewDynamicRecommendation(invest500DR);

        assertEquals(expectedDynamicRecommendationDTO, actualDynamicRecommendationDTO);
        verify(checkRecommendation, times(1)).checkRecommendationCorrect(invest500DR);
        verify(recommendationsRepository, times(1)).save(invest500DR);
        verify(statisticRepository, times(1)).save(any(Statistic.class));
        verify(rulesRepository, times(1)).save(rule1);
        verify(rulesRepository, times(1)).save(rule2);
        verify(rulesRepository, times(1)).save(rule3);
    }

}
