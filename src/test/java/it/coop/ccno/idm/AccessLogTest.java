package it.coop.ccno.idm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccessLogTest {

    @Test
    void formatsHttpRequestDetails() {
        assertEquals(
                "http_request method=GET path=/health environment=local-test status=200 duration_ms=12 remote=127.0.0.1",
                AccessLog.format("GET", "/health", "local-test", 200, 12, "127.0.0.1"));
    }
}
