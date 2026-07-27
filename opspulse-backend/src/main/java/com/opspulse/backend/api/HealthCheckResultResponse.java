package com.opspulse.backend.api;

import com.opspulse.backend.check.HealthCheckResult;
import com.opspulse.backend.check.HealthStatus;
import java.time.Instant;

public record HealthCheckResultResponse(Long id, Long serviceId, String serviceName, HealthStatus status,
                                        Integer httpStatus, long responseTimeMs, String errorMessage, Instant checkedAt) {
    public static HealthCheckResultResponse from(HealthCheckResult result) {
        return new HealthCheckResultResponse(result.getId(), result.getService().getId(), result.getService().getName(),
                result.getStatus(), result.getHttpStatus(), result.getResponseTimeMs(), result.getErrorMessage(),
                result.getCheckedAt());
    }
}
