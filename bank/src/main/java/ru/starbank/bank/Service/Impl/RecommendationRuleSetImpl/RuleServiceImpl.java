package ru.starbank.bank.Service.Impl.RecommendationRuleSetImpl;

import org.springframework.stereotype.Service;
import ru.starbank.bank.Model.DynamicRecommendation;
import ru.starbank.bank.Repository.RulesRepository;
import ru.starbank.bank.Service.RuleService;

import java.util.List;

@Service
public class RuleServiceImpl implements RuleService {

    RulesRepository repository;

    @Override
    public DynamicRecommendation createNewDynamicRecommendation(DynamicRecommendation recommendation) {
        return repository.createNewDynamicRecommendation(recommendation);
    }

    @Override
    public List<DynamicRecommendation> getAllDynamicRecommendations() {
        return repository.getAllDynamicRecommendations();
    }

    @Override
    public void deleteDynamicRecommendation(Long recommendationId) {
        return repository.deleteDynamicRecommendation(recommendationId);
    }

}
