package com.opspulse.payment.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Contributes this service's state to Spring Boot's /actuator/health response.
 * Spring invokes every HealthIndicator and combines their results.
 */
@Component
public class DemoPaymentHealthIndicator implements HealthIndicator {

    private static final long SLOW_RESPONSE_DELAY_MS = 2_000;

    private final DemoHealthState healthState;

    public DemoPaymentHealthIndicator(DemoHealthState healthState) {
        this.healthState = healthState;
    }

    @Override
    public Health health() {
        DemoHealthMode mode = healthState.getMode();

        if (mode == DemoHealthMode.DOWN) {
            return Health.down()
                    .withDetail("mode", mode)
                    .withDetail("reason", "Payment service failure simulation is enabled")
                    .build();
        }

        if (mode == DemoHealthMode.SLOW) {
            simulateSlowResponse();
            return Health.up()
                    .withDetail("mode", mode)
                    .withDetail("message", "The service is healthy but responding slowly")
                    .build();
        }

        return Health.up()
                .withDetail("mode", mode)
                .build();
    }

    private void simulateSlowResponse() {
        try {
            Thread.sleep(SLOW_RESPONSE_DELAY_MS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
