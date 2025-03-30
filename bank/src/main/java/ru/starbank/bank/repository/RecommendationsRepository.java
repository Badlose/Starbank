package ru.starbank.bank.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.starbank.bank.model.DynamicRecommendation;

@Repository
public interface RecommendationsRepository extends JpaRepository<DynamicRecommendation, Long> {

    @Cacheable(value = "DynamicRecommendationCache", key = "#name")
    DynamicRecommendation findByName(String name);

}
