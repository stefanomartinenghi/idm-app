package it.coop.ccno.idm.controller;

import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Compatibilita' temporanea durante la migrazione progressiva delle probe di qual e prod.
 * Le nuove probe Kubernetes usano gli endpoint dedicati di Spring Boot Actuator.
 */
@RestController
public class LegacyHealthController {

    private final ApplicationAvailability availability;

    public LegacyHealthController(ApplicationAvailability availability) {
        this.availability = availability;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        boolean available = availability.getLivenessState() == LivenessState.CORRECT
                && availability.getReadinessState() == ReadinessState.ACCEPTING_TRAFFIC;

        HttpStatus status = available ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        String bodyStatus = available ? "UP" : "DOWN";
        return ResponseEntity.status(status).body(Map.of("status", bodyStatus));
    }
}
