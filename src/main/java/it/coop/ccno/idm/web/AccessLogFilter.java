package it.coop.ccno.idm.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessLogFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogFilter.class);
    private static final String ACTUATOR_HEALTH_PATH = "/actuator/health";
    private static final String LEGACY_HEALTH_PATH = "/health";

    private final String environment;

    public AccessLogFilter(@Value("${app.environment:local}") String environment) {
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        long startedAt = System.nanoTime();

        try {
            filterChain.doFilter(request, response);
        } finally {
            int status = response.getStatus();

            if (shouldLog(request.getRequestURI(), status)) {
                long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
                LOGGER.info(
                        "http_request method={} path={} environment={} status={} duration_ms={} remote={}",
                        request.getMethod(),
                        request.getRequestURI(),
                        environment,
                        status,
                        durationMillis,
                        request.getRemoteAddr());
            }
        }
    }

    static boolean shouldLog(String path, int status) {
        boolean healthRequest = path.startsWith(ACTUATOR_HEALTH_PATH) || path.equals(LEGACY_HEALTH_PATH);
        boolean successfulHealthRequest = healthRequest && status >= 200 && status < 400;
        return !successfulHealthRequest;
    }
}
