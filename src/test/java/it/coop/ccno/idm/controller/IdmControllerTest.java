package it.coop.ccno.idm.controller;

import it.coop.ccno.idm.service.IdmService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdmControllerTest {

    @Test
    void delegatesApplicationInfoToService() {
        var response = new IdmController(new IdmService("local-qual")).getApplicationInfo();

        assertThat(response.application()).isEqualTo("idm-app");
        assertThat(response.environment()).isEqualTo("local-qual");
    }
}
