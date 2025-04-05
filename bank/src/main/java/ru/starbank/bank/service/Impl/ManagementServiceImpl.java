package ru.starbank.bank.service.Impl;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;
import ru.starbank.bank.dto.InfoDTO;
import ru.starbank.bank.service.ManagementService;

@Service
public class ManagementServiceImpl implements ManagementService {

    @Autowired
    private BuildProperties buildProperties;

    @Override
    public InfoDTO getServiceInfo() {

        String artifact = buildProperties.getArtifact();
        String version = buildProperties.getVersion();
        return new InfoDTO(artifact, version);
    }

    @Override
    public void clearAllCache() {

    }
}
