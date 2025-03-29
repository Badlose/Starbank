package ru.starbank.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.starbank.bank.model.Rule;

@Repository
public interface RulesRepository extends JpaRepository<Rule, Long> {


}
