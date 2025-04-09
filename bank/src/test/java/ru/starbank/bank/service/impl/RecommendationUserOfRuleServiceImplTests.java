package ru.starbank.bank.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.Impl.RecommendationUserOfRuleServiceImpl;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RecommendationUserOfRuleServiceImplTests {
    @InjectMocks
    RecommendationUserOfRuleServiceImpl service;
    @Mock
    TransactionsRepository repository;

    @Test
    public void shouldReturnCheck(){

    }
}
