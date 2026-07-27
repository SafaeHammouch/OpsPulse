package com.opspulse.backend.check;

import com.opspulse.backend.service.MonitoredService;
import com.opspulse.backend.service.MonitoredServiceRepository;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Runs checks only for enabled services whose configured interval has elapsed. */
@Component
public class HealthCheckScheduler {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckScheduler.class);

    private final MonitoredServiceRepository monitoredServiceRepository;
    private final HealthCheckResultRepository healthCheckResultRepository;
    private final HealthCheckService healthCheckService;

    public HealthCheckScheduler(MonitoredServiceRepository monitoredServiceRepository,
                                HealthCheckResultRepository healthCheckResultRepository,
                                HealthCheckService healthCheckService) {
        this.monitoredServiceRepository = monitoredServiceRepository;
        this.healthCheckResultRepository = healthCheckResultRepository;
        this.healthCheckService = healthCheckService;
    }

    @Scheduled(fixedDelayString = "${opspulse.health.scheduler-delay-ms:5000}")
    public void checkDueServices() {
        Instant now = Instant.now();
        monitoredServiceRepository.findAll().stream()
                .filter(MonitoredService::isEnabled)
                .filter(service -> isDue(service, now))
                .forEach(service -> checkService(service.getId()));
    }

    private boolean isDue(MonitoredService service, Instant now) {
        return healthCheckResultRepository.findTopByServiceIdOrderByCheckedAtDesc(service.getId())
                .map(result -> !result.getCheckedAt()
                        .plusSeconds(service.getCheckIntervalSeconds())
                        .isAfter(now))
                .orElse(true);
    }

    private void checkService(Long serviceId) {
        try {
            healthCheckService.check(serviceId);
        } catch (RuntimeException exception) {
            log.warn("Automatic health check failed for service {}", serviceId, exception);
        }
    }
}
