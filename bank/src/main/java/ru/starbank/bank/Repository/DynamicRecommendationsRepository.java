package ru.starbank.bank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.starbank.bank.Model.DynamicRecommendation;

@Repository
public interface DynamicRecommendationsRepository extends JpaRepository<DynamicRecommendation, Long> {
}
