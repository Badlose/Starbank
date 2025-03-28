package ru.starbank.bank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.starbank.bank.Model.DynamicRecommendation;

@Repository
public interface RecommendationsRepository extends JpaRepository<DynamicRecommendation, Long> {

    DynamicRecommendation findByName(String name);

}
