package ru.starbank.bank.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starbank.bank.Model.Recommendation;
import ru.starbank.bank.Service.RecommendationRuleSet;
import ru.starbank.bank.Service.RecommendationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationServiceImpl implements RecommendationService {


    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {

        //List<Recommendation> //бины

        return Optional.empty();
    }

}
