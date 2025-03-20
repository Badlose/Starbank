package ru.starbank.bank.Model;

import org.springframework.stereotype.Component;
import ru.starbank.bank.Repository.RecommendationsRepository;
import ru.starbank.bank.Service.Impl.RuleSetOne;
import ru.starbank.bank.Service.Impl.RuleSetTwo;
import ru.starbank.bank.Service.RecommendationService;

import java.util.Optional;
import java.util.UUID;

@Component
public class ModelRecommendation implements RecommendationService {

    private final RecommendationsRepository repository;

    public ModelRecommendation(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Recommendation> getRecommendation(UUID userId) {
        String result;

        if (RuleSetOne.check()) {
            result += "Recomend 1";
        }

        if (RuleSetTwo.check()) {
            result += "Recomend 2";
        }

        if (RuleSetThree.check()) {
            result += "Recomend 3";
        }
        return Optional.empty();
    }

    @Override
    public Integer getAmount(UUID userId) {

        System.out.println(RuleSetOne.getAmount(userId));
        System.out.println(RuleSetTwo.getAmount(userId));
        return RuleSetOne.getAmount(userId);
    }


//
//    должен сделать проверки
//
//    проверки отдельные классы
//
//    должен вернуть массив


}
