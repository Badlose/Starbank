package ru.starbank.bank.telegram.service;

import java.util.UUID;

public interface MessageService {

    String getFirstNameLastNameByUserName(String userName);

    UUID getUserIdByUsername(String userName);

}
