package ru.starbank.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.starbank.bank.model.Statistic;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    Statistic findByRecommendationId(Long id);
}
