package com.opspulse.backend.api;

import com.opspulse.backend.service.Criticality;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record CreateMonitoredServiceRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 2_000) @URL(message = "healthUrl must be a valid URL") String healthUrl,
        @NotNull Criticality criticality,
        @Min(1) @Max(86_400) int checkIntervalSeconds) {
}
