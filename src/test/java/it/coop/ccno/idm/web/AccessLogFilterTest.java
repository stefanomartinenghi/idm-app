package it.coop.ccno.idm.web;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccessLogFilterTest {

    @Test
    void skipsSuccessfulHealthRequests() {
        assertThat(AccessLogFilter.shouldLog("/actuator/health/liveness", 200)).isFalse();
        assertThat(AccessLogFilter.shouldLog("/actuator/health/readiness", 200)).isFalse();
        assertThat(AccessLogFilter.shouldLog("/health", 200)).isFalse();
    }

    @Test
    void logsFailedHealthRequestsAndApplicationRequests() {
        assertThat(AccessLogFilter.shouldLog("/actuator/health/readiness", 503)).isTrue();
        assertThat(AccessLogFilter.shouldLog("/health", 503)).isTrue();
        assertThat(AccessLogFilter.shouldLog("/api/idm", 200)).isTrue();
    }
}
