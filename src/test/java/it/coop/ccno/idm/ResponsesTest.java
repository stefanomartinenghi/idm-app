package it.coop.ccno.idm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponsesTest {

    @Test
    void returnsTheSelectedEnvironment() {
        assertEquals(
                "{\"application\":\"idm-app\",\"environment\":\"local-test\"}",
                Responses.home("local-test"));
    }

    @Test
    void reportsHealthyStatus() {
        assertEquals("{\"status\":\"UP\"}", Responses.health());
    }
}
