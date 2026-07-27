package com.opspulse.backend.check;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opspulse.backend.api.ServiceNotFoundException;
import com.opspulse.backend.service.MonitoredService;
import com.opspulse.backend.service.MonitoredServiceRepository;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Service
public class HealthCheckService {

    private final MonitoredServiceRepository monitoredServiceRepository;
    private final HealthCheckResultRepository healthCheckResultRepository;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final long slowResponseThresholdMs;

    public HealthCheckService(MonitoredServiceRepository monitoredServiceRepository,
                              HealthCheckResultRepository healthCheckResultRepository,
                              RestClient.Builder restClientBuilder,
                              ObjectMapper objectMapper,
                              @Value("${opspulse.health.slow-response-threshold-ms:1000}") long slowResponseThresholdMs) {
        this.monitoredServiceRepository = monitoredServiceRepository;
        this.healthCheckResultRepository = healthCheckResultRepository;
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.slowResponseThresholdMs = slowResponseThresholdMs;
    }

    public HealthCheckResult check(Long serviceId) {
        MonitoredService service = monitoredServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));

        long startedAt = System.nanoTime();
        HealthStatus status;
        Integer httpStatus = null;
        String errorMessage = null;

        try {
            ResponseEntity<String> response = restClient.get()
                    .uri(service.getHealthUrl())
                    .retrieve()
                    .toEntity(String.class);
            httpStatus = response.getStatusCode().value();
            status = determineStatus(response.getStatusCode(), response.getBody());
            if (status == HealthStatus.DOWN) {
                errorMessage = "Service reported DOWN";
            }
        } catch (RestClientResponseException exception) {
            httpStatus = exception.getStatusCode().value();
            status = determineStatus(exception.getStatusCode(), exception.getResponseBodyAsString());
            errorMessage = status == HealthStatus.DOWN ? "Service reported DOWN" : rootMessage(exception);
        } catch (RestClientException exception) {
            status = HealthStatus.UNREACHABLE;
            errorMessage = rootMessage(exception);
        }

        long responseTimeMs = Duration.ofNanos(System.nanoTime() - startedAt).toMillis();
        if (status == HealthStatus.UP && responseTimeMs >= slowResponseThresholdMs) {
            status = HealthStatus.SLOW;
            errorMessage = "Response time exceeded the " + slowResponseThresholdMs + " ms slow threshold";
        }
        return healthCheckResultRepository.save(
                new HealthCheckResult(service, status, httpStatus, responseTimeMs, errorMessage));
    }

    private HealthStatus determineStatus(HttpStatusCode httpStatus, String responseBody) {
        try {
            JsonNode body = objectMapper.readTree(responseBody);
            if ("UP".equalsIgnoreCase(body.path("status").asText())) {
                return HealthStatus.UP;
            }
            return HealthStatus.DOWN;
        } catch (Exception ignored) {
            return httpStatus.is2xxSuccessful() ? HealthStatus.UP : HealthStatus.DOWN;
        }
    }

    private String rootMessage(Exception exception) {
        Throwable cause = exception;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage() == null ? cause.getClass().getSimpleName() : cause.getMessage();
    }
}
