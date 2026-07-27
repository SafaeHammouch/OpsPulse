package com.opspulse.backend.api;

import com.opspulse.backend.service.Criticality;
import com.opspulse.backend.service.MonitoredService;
import java.time.Instant;

public record MonitoredServiceResponse(Long id, String name, String healthUrl, Criticality criticality,
                                       int checkIntervalSeconds, boolean enabled, Instant createdAt) {
    public static MonitoredServiceResponse from(MonitoredService service) {
        return new MonitoredServiceResponse(service.getId(), service.getName(), service.getHealthUrl(),
                service.getCriticality(), service.getCheckIntervalSeconds(), service.isEnabled(), service.getCreatedAt());
    }
}
