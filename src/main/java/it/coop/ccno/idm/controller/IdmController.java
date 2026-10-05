package it.coop.ccno.idm.controller;

import it.coop.ccno.idm.dto.IdmResponse;
import it.coop.ccno.idm.service.IdmService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdmController {

    private final IdmService idmService;

    public IdmController(IdmService idmService) {
        this.idmService = idmService;
    }

    @GetMapping(value = {"/", "/api/idm", "/prova-browser"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResponse getApplicationInfo() {
        return idmService.getApplicationInfo();
    }
}
