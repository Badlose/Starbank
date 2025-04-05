package ru.starbank.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.starbank.bank.dto.InfoDTO;
import ru.starbank.bank.service.ManagementService;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final ManagementService service;

    public ManagementController(ManagementService service) {
        this.service = service;
    }

    @GetMapping("/info")
    public InfoDTO getServiceInfo() {
        return service.getServiceInfo();
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<HttpStatus> clearAllCache() {
        service.clearAllCaches();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
