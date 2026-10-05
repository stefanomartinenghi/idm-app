package it.coop.ccno.idm.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdmServiceTest {

    @Test
    void returnsApplicationInfoForConfiguredEnvironment() {
        var response = new IdmService("local-test").getApplicationInfo();

        assertThat(response.application()).isEqualTo("idm-app");
        assertThat(response.environment()).isEqualTo("local-test");
    }
}
