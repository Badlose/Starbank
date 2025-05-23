package ru.starbank.bank.staticRecommendations;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.starbank.bank.exceptions.StaticRecommendationInitializerException;
import ru.starbank.bank.model.DynamicRecommendation;
import ru.starbank.bank.model.Rule;
import ru.starbank.bank.model.Statistic;
import ru.starbank.bank.repository.RecommendationsRepository;
import ru.starbank.bank.repository.RulesRepository;
import ru.starbank.bank.repository.StatisticRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class StaticRecommendationDBInitializer {

    private final RecommendationsRepository recommendationsRepository;
    private final RulesRepository rulesRepository;
    private final StatisticRepository statisticRepository;

    private static final String INVEST500ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private static final String INVEST500NAME = "Invest500";
    private static final String INVEST500TEXT = "\nОткройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! " +
            "Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. " +
            "Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private static final String TOPSAVINGID = "59efc529-2fff-41af-baff-90ccd7402925";
    private static final String TOPSAVINGNAME = "TopSaving";
    private static final String TOPSAVINGTEXT = """

            Откройте свою собственную «Копилку» с нашим банком! \
            «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. \
            Больше никаких забытых чеков и потерянных квитанций — всё под контролем!

            Преимущества «Копилки»:

            Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.

            Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.

            Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.

            Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""";

    private static final String SIMPLELOANID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private static final String SIMPLELOANNAME = "Простой кредит";
    private static final String SIMPLELOANTEXT = """
                        
            Откройте мир выгодных кредитов с нами!

            Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.

            Почему выбирают нас:

            Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.

            Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.

            Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.

            Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!""";

    private static final List<Rule> INVEST500RULES = new ArrayList<>(List.of(
            new Rule("USER_OF", List.of("DEBIT"), true),
            new Rule("USER_OF", List.of("INVEST"), false),
            new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000"), true)
    ));
    private static final List<Rule> TOPSAVINGRULES = new ArrayList<>(List.of(
            new Rule("USER_OF", List.of("DEBIT"), true),
            new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">=", "50000", "OR"), true),
            new Rule("TRANSACTION_SUM_COMPARE", List.of("SAVING", "DEPOSIT", ">=", "50000", "OR"), true),
            new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true) // +
    ));
    private static final List<Rule> SIMPLELOANRULES = new ArrayList<>(List.of(
            new Rule("USER_OF", List.of("CREDIT"), false),
            new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), true),
            new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "WITHDRAW", ">", "100000"), true)
    ));

    public StaticRecommendationDBInitializer(RecommendationsRepository recommendationsRepository, RulesRepository rulesRepository, StatisticRepository statisticRepository) {
        this.recommendationsRepository = recommendationsRepository;
        this.rulesRepository = rulesRepository;
        this.statisticRepository = statisticRepository;
    }

    List<DynamicRecommendation> recommendationList = new ArrayList<>(List.of(
            new DynamicRecommendation(INVEST500NAME, UUID.fromString(INVEST500ID), INVEST500TEXT, INVEST500RULES),
            new DynamicRecommendation(TOPSAVINGNAME, UUID.fromString(TOPSAVINGID), TOPSAVINGTEXT, TOPSAVINGRULES),
            new DynamicRecommendation(SIMPLELOANNAME, UUID.fromString(SIMPLELOANID), SIMPLELOANTEXT, SIMPLELOANRULES)
    ));

    //какой тут может быть негатив? напрашивается исключение
    @PostConstruct
    @Transactional
    public void initializeStaticRecommendations() {
        try {
            for (DynamicRecommendation recommendation : recommendationList) {
                DynamicRecommendation existingRecommendations = recommendationsRepository.findByName(recommendation.getName());
                if (existingRecommendations == null) {

                    try {
                        recommendationsRepository.save(recommendation);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    Statistic statistic = new Statistic(recommendation.getId(), 0);
                    try {
                        statisticRepository.save(statistic);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    for (Rule rule : recommendation.getRuleList()) {
                        rule.setDynamicRecommendation(recommendation);
                        try {
                            rulesRepository.save(rule);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            throw new StaticRecommendationInitializerException("Exception when initializing static recommendations");
        }
    }

}