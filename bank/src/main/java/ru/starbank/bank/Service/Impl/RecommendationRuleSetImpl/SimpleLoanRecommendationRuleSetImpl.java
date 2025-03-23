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
public class SimpleLoanRecommendationRuleSetImpl implements RecommendationRuleSet {

    private final List<CheckConditionService> conditionServices;

    private static final Logger logger = LoggerFactory.getLogger(SimpleLoanRecommendationRuleSetImpl.class);

    public SimpleLoanRecommendationRuleSetImpl(@Qualifier("CreditNotUsing") CheckConditionService checkRuleOne,
                                               @Qualifier("DebitDepositMoreThanWithdraw") CheckConditionService checkRuleTwo,
                                               @Qualifier("DebitWithdrawMore100_000") CheckConditionService checkRulThree) {
        this.conditionServices = List.of(checkRuleOne, checkRuleTwo, checkRulThree);
    }

    private static final String ID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private static final String NAME = "Простой кредит";
    private static final String TEXT = """
            Откройте мир выгодных кредитов с нами!

            Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! \
            Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.

            Почему выбирают нас:

            Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.

            Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.

            Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.

            Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!""";

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
            logger.info("The Simple Loan product is suitable for the user {}", userId);
            return Optional.of(RECOMMENDATION);
        }
        logger.info("The Simple Loan product was not suitable for the user {}", userId);
        return Optional.empty();
    }

}


