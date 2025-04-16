package ru.starbank.bank.TestController;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.starbank.bank.dto.*;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.model.Statistic;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.repository.RulesRepository;
import ru.starbank.bank.repository.StatisticRepository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecommendationControllerTestRestTemplate {
    @Autowired
    private RecommendationsRepository recommendationsRepository;
    @Autowired
    private RulesRepository rulesRepository;
    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private final UUID INVEST500ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private final String INVEST500NAME = "Invest500";
    private final String INVEST500TEXT = "\nОткройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! " +
            "Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. " +
            "Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private final UUID TOPSAVINGID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private final String TOPSAVINGNAME = "TopSaving";
    private final String TOPSAVINGTEXT = """
            Откройте свою собственную «Копилку» с нашим банком! \
            «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. \
            Больше никаких забытых чеков и потерянных квитанций — всё под контролем!

            Преимущества «Копилки»:

            Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.

            Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.

            Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.

            Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""";

    private final UUID SIMPLELOANID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
    private final String SIMPLELOANNAME = "Простой кредит";
    private final String SIMPLELOANTEXT = """
                        
            Откройте мир выгодных кредитов с нами!

            Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.

            Почему выбирают нас:

            Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.

            Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.

            Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.

            Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!""";
    private final Rule rule1 = new Rule("USER_OF", List.of("DEBIT"), true);
    private final Rule rule2 = new Rule("USER_OF", List.of("INVEST"), false);
    private final Rule rule3 = new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000"), true);
    private final Rule rule4 = new Rule("USER_OF", List.of("DEBIT"), true);
    private final Rule rule5 = new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">=", "50000", "OR"), true);
    private final Rule rule6 = new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000", "OR"), true);
    private final Rule rule7 = new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true);
    private final Rule rule8 = new Rule("USER_OF", List.of("CREDIT"), false);
    private final Rule rule9 = new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true);
    private final Rule rule10 = new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "WITHDRAW", ">", "100000"), true);

    private final List<Rule> INVEST500RULES = new ArrayList<>(List.of(rule1, rule2, rule3));
    private final List<Rule> TOPSAVINGRULES = new ArrayList<>(List.of(rule4, rule5, rule6, rule7));
    private final List<Rule> SIMPLELOANRULES = new ArrayList<>(List.of(rule8, rule9, rule10));

    private final DynamicRecommendation recommendation1 = new DynamicRecommendation(INVEST500NAME, INVEST500ID, INVEST500TEXT, INVEST500RULES);
    private final DynamicRecommendation recommendation2 = new DynamicRecommendation(TOPSAVINGNAME, TOPSAVINGID, TOPSAVINGTEXT, TOPSAVINGRULES);
    private final DynamicRecommendation recommendation3 = new DynamicRecommendation(SIMPLELOANNAME, SIMPLELOANID, SIMPLELOANTEXT, SIMPLELOANRULES);

    private Long id1;
    private Long id2;
    private Long id3;


    @BeforeEach
    public void create() {
        rulesRepository.deleteAll();
        statisticRepository.deleteAll();
        recommendationsRepository.deleteAll();


        DynamicRecommendation createdRecommendation1 = recommendationsRepository.save(recommendation1);
        DynamicRecommendation createdRecommendation2 = recommendationsRepository.save(recommendation2);
        DynamicRecommendation createdRecommendation3 = recommendationsRepository.save(recommendation3);

        id1 = createdRecommendation1.getId();
        id2 = createdRecommendation2.getId();
        id3 = createdRecommendation3.getId();

        rule1.setDynamicRecommendation(createdRecommendation1);
        rule2.setDynamicRecommendation(createdRecommendation1);
        rule3.setDynamicRecommendation(createdRecommendation1);
        rule4.setDynamicRecommendation(createdRecommendation2);
        rule5.setDynamicRecommendation(createdRecommendation2);
        rule6.setDynamicRecommendation(createdRecommendation2);
        rule7.setDynamicRecommendation(createdRecommendation2);
        rule8.setDynamicRecommendation(createdRecommendation3);
        rule9.setDynamicRecommendation(createdRecommendation3);
        rule10.setDynamicRecommendation(createdRecommendation3);

        rulesRepository.save(rule1);
        rulesRepository.save(rule2);
        rulesRepository.save(rule3);
        rulesRepository.save(rule4);
        rulesRepository.save(rule5);
        rulesRepository.save(rule6);
        rulesRepository.save(rule7);
        rulesRepository.save(rule8);
        rulesRepository.save(rule9);
        rulesRepository.save(rule10);

        Statistic statistic1 = new Statistic(createdRecommendation1.getId(), 0);
        Statistic statistic2 = new Statistic(createdRecommendation2.getId(), 0);
        Statistic statistic3 = new Statistic(createdRecommendation3.getId(), 0);

        statisticRepository.save(statistic1);
        statisticRepository.save(statistic2);
        statisticRepository.save(statistic3);
    }

    @Test
    void shouldGetRecommendation() {

        String userId = "cd515076-5d8a-44be-930e-8d4fcb79f42d";


        ResponseEntity<UserRecommendationsDTO> userRecommendationsEntityGet = restTemplate.getForEntity(
                "http://localhost:" + port + "/recommendation/userId/" + userId,
                UserRecommendationsDTO.class
        );

        UserDTO expectedUserDTO1 = new UserDTO(INVEST500NAME, INVEST500ID, INVEST500TEXT);
        UserDTO expectedUserDTO2 = new UserDTO(TOPSAVINGNAME, TOPSAVINGID, TOPSAVINGTEXT);

        Assertions.assertNotNull(userRecommendationsEntityGet);
        Assertions.assertEquals(userRecommendationsEntityGet.getStatusCode(), HttpStatusCode.valueOf(200));

        UserRecommendationsDTO actualUserRecommendations = userRecommendationsEntityGet.getBody();

        Assertions.assertNotNull(actualUserRecommendations.getUserId());
        Assertions.assertEquals(actualUserRecommendations.getUserId().toString(), userId);
        Assertions.assertNotNull(actualUserRecommendations.getRecommendations());
        Assertions.assertEquals(actualUserRecommendations.getRecommendations().toString(), List.of(expectedUserDTO1, expectedUserDTO2).toString());
    }

    @Test
    void shouldCreateRecommendation() {
        Rule ruleForCreate1 = new Rule("USER_OF", List.of("DEBIT"), true);
        Rule ruleForCreate2 = new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">=", "50000"), true);
        RuleDTO ruleDTO1 = new RuleDTO(ruleForCreate1.getQuery(), ruleForCreate1.getArguments(), ruleForCreate1.isNegate());
        RuleDTO ruleDTO2 = new RuleDTO(ruleForCreate2.getQuery(), ruleForCreate2.getArguments(), ruleForCreate2.isNegate());
        DynamicRecommendation expectedRecommendation = new DynamicRecommendation("name", UUID.randomUUID(), "text", List.of(ruleForCreate1, ruleForCreate2));


        ResponseEntity<DynamicRecommendationDTO> userRecommendationsEntityGet = restTemplate.postForEntity(
                "http://localhost:" + port + "/recommendation/rule",
                expectedRecommendation,
                DynamicRecommendationDTO.class
        );

        Assertions.assertNotNull(userRecommendationsEntityGet);
        Assertions.assertEquals(userRecommendationsEntityGet.getStatusCode(), HttpStatusCode.valueOf(200));

        DynamicRecommendationDTO actualRecommendation = userRecommendationsEntityGet.getBody();
        List<RuleDTO> expectedRuleList = List.of(ruleDTO1, ruleDTO2);

        Assertions.assertNotNull(actualRecommendation.getId());
        Assertions.assertEquals(actualRecommendation.getProduct_name(), expectedRecommendation.getName());
        Assertions.assertEquals(actualRecommendation.getProduct_id(), expectedRecommendation.getProductId());
        Assertions.assertNotNull(actualRecommendation.getRule());
        Assertions.assertEquals(actualRecommendation.getRule().toString(), expectedRuleList.toString());
    }

    @Test
    void shouldGetAllRecommendation() {

        RuleDTO ruleDTO1 = new RuleDTO(rule1.getQuery(), rule1.getArguments(), rule1.isNegate());
        RuleDTO ruleDTO2 = new RuleDTO(rule2.getQuery(), rule2.getArguments(), rule2.isNegate());
        RuleDTO ruleDTO3 = new RuleDTO(rule3.getQuery(), rule3.getArguments(), rule3.isNegate());
        RuleDTO ruleDTO4 = new RuleDTO(rule4.getQuery(), rule4.getArguments(), rule4.isNegate());
        RuleDTO ruleDTO5 = new RuleDTO(rule5.getQuery(), rule5.getArguments(), rule5.isNegate());
        RuleDTO ruleDTO6 = new RuleDTO(rule6.getQuery(), rule6.getArguments(), rule6.isNegate());
        RuleDTO ruleDTO7 = new RuleDTO(rule7.getQuery(), rule7.getArguments(), rule7.isNegate());
        RuleDTO ruleDTO8 = new RuleDTO(rule8.getQuery(), rule8.getArguments(), rule8.isNegate());
        RuleDTO ruleDTO9 = new RuleDTO(rule9.getQuery(), rule9.getArguments(), rule9.isNegate());
        RuleDTO ruleDTO10 = new RuleDTO(rule10.getQuery(), rule10.getArguments(), rule10.isNegate());

        List<RuleDTO> ruleDTOList1 = List.of(ruleDTO1, ruleDTO2, ruleDTO3);
        List<RuleDTO> ruleDTOList2 = List.of(ruleDTO4, ruleDTO5, ruleDTO6, ruleDTO7);
        List<RuleDTO> ruleDTOList3 = List.of(ruleDTO8, ruleDTO9, ruleDTO10);


        DynamicRecommendationDTO recommendationDTO1 = new DynamicRecommendationDTO(id1, recommendation1.getName(),
                recommendation1.getProductId(), recommendation1.getText(), ruleDTOList1);
        DynamicRecommendationDTO recommendationDTO2 = new DynamicRecommendationDTO(id2, recommendation2.getName(),
                recommendation2.getProductId(), recommendation2.getText(), ruleDTOList2);
        DynamicRecommendationDTO recommendationDTO3 = new DynamicRecommendationDTO(id3, recommendation3.getName(),
                recommendation3.getProductId(), recommendation3.getText(), ruleDTOList3);

        ListDynamicRecommendationDTO expectedListDynamicRecommendationDTO = new ListDynamicRecommendationDTO(
                List.of(recommendationDTO1, recommendationDTO2, recommendationDTO3));

        ResponseEntity<ListDynamicRecommendationDTO> allRecommendations = restTemplate.getForEntity(
                "http://localhost:" + port + "/recommendation/rule",
                ListDynamicRecommendationDTO.class
        );

        Assertions.assertNotNull(allRecommendations);
        Assertions.assertEquals(allRecommendations.getStatusCode(), HttpStatusCode.valueOf(200));

        ListDynamicRecommendationDTO actualAllRecommendations = allRecommendations.getBody();

        Assertions.assertNotNull(actualAllRecommendations.getData());
        Assertions.assertEquals(actualAllRecommendations.toString(), expectedListDynamicRecommendationDTO.toString());

    }

    @Test
    void shouldDeleteRecommendation() {
        Assertions.assertNotNull(recommendationsRepository.findById(id1).orElse(null));

        restTemplate.delete("http://localhost:" + port + "/recommendation/rule/"+id1);

        Assertions.assertNull(recommendationsRepository.findById(id1).orElse(null));

    }

    @Test
    void shouldGetStatistic() {
        Rule rule1 = new Rule("USER_OF", List.of("DEBIT"), true);
        Rule rule2 = new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">=", "50000"), true);

        DynamicRecommendation recommendation = new DynamicRecommendation("name", UUID.randomUUID(), "text", List.of(rule1, rule2));


        ResponseEntity<DynamicRecommendationDTO> userRecommendationsEntityGet = restTemplate.postForEntity(
                "http://localhost:" + port + "/recommendation/rule",
                recommendation,
                DynamicRecommendationDTO.class
        );

        Assertions.assertNotNull(userRecommendationsEntityGet);
        Assertions.assertEquals(userRecommendationsEntityGet.getStatusCode(), HttpStatusCode.valueOf(200));

        DynamicRecommendationDTO actualUserRecommendations = userRecommendationsEntityGet.getBody();

        Assertions.assertNotNull(actualUserRecommendations.getId());
//        Assertions.assertEquals(actualUserRecommendations.getUserId().toString(), userId);
//        Assertions.assertNotNull(actualUserRecommendations.getRecommendations());
//        Assertions.assertEquals(actualUserRecommendations.getRecommendations(), List.of());
    }

}


