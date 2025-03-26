package ru.starbank.bank.Service.Impl.RecommendationRuleSetImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.CheckConditionService;
import ru.starbank.bank.Service.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRecommendationRuleSetImpl implements RecommendationRuleSet {

    private final List<CheckConditionService> conditionServices;

    private static final Logger logger = LoggerFactory.getLogger(TopSavingRecommendationRuleSetImpl.class);

    public TopSavingRecommendationRuleSetImpl(@Qualifier("DebitUsing") CheckConditionService checkRuleOne,
                                              @Qualifier("OrDebitOrSavingDepositMoreOrEqual50_000") CheckConditionService checkRuleTwo,
                                              @Qualifier("DebitDepositMoreThanWithdraw") CheckConditionService checkRuleThree) {
        this.conditionServices = List.of(checkRuleOne, checkRuleTwo, checkRuleThree);
    }

    private static final String ID = "59efc529-2fff-41af-baff-90ccd7402925";
    private static final String NAME = "TopSaving";
    private static final String TEXT = """
            Откройте свою собственную «Копилку» с нашим банком! \
            «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. \
            Больше никаких забытых чеков и потерянных квитанций — всё под контролем!

            Преимущества «Копилки»:

            Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.

            Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.

            Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.

            Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""";

    private static final Recommendation RECOMMENDATION = new Recommendation(
            UUID.fromString(ID),
            NAME,
            TEXT

    );


    @Override
    public Optional<Recommendation> check(UUID userId) {

        boolean checkCondition = conditionServices.stream()
                .allMatch(r -> r.checkCondition(userId));

        if (checkCondition) {
            logger.info("The Top Saving product is suitable for the user {}", userId);
            return Optional.of(RECOMMENDATION);
        }
        logger.info("The Top Saving product was not suitable for the user {}", userId);
        return Optional.empty();
    }

}


