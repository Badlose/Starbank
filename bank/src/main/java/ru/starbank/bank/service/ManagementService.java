package ru.starbank.bank.service;

import ru.starbank.bank.dto.InfoDTO;

public interface ManagementService {

    InfoDTO getServiceInfo();

    void clearAllCaches();
}
