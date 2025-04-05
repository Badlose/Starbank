package ru.starbank.bank.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import ru.starbank.bank.dto.InfoDTO;
import ru.starbank.bank.repository.TransactionsRepository;
import ru.starbank.bank.service.ManagementService;

import java.util.Objects;

@Service
public class ManagementServiceImpl implements ManagementService {

    private static final Logger logger = LoggerFactory.getLogger(ManagementServiceImpl.class);
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private BuildProperties buildProperties;

    private final TransactionsRepository repository;

    public ManagementServiceImpl(TransactionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public InfoDTO getServiceInfo() {

        String artifact = buildProperties.getArtifact();
        String version = buildProperties.getVersion();
        return new InfoDTO(artifact, version);
    }

    @Override
    public void clearAllCaches() {
        logger.info(cacheManager.getCacheNames().toString());
        logger.info(Objects.requireNonNull(cacheManager.getCache("transactionSumCompareDepositWithdraw")).toString());
        repository.clearCache();
        logger.info(cacheManager.getCacheNames().toString());
        logger.info(Objects.requireNonNull(cacheManager.getCache("transactionSumCompareDepositWithdraw")).getClass().toString());
    }
}
