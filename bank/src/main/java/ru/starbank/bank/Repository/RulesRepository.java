package ru.starbank.bank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.starbank.bank.Model.DynamicRecommendation;
import ru.starbank.bank.Model.Rule;

@Repository
public interface RulesRepository extends JpaRepository<Rule, Long> {


}
