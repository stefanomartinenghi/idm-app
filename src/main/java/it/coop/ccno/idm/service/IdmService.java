package it.coop.ccno.idm.service;

import it.coop.ccno.idm.dto.IdmResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IdmService {

    private final String environment;

    public IdmService(@Value("${app.environment:local}") String environment) {
        this.environment = environment;
    }

    public IdmResponse getApplicationInfo() {
        return new IdmResponse("idm-app", environment);
    }
}
