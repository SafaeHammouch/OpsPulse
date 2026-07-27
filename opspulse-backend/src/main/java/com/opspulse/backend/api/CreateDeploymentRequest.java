package com.opspulse.backend.api;

import com.opspulse.backend.deployment.DeploymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDeploymentRequest(
        @NotNull Long serviceId,
        @NotBlank @Size(max = 120) String version,
        @NotBlank @Size(max = 60) String environment,
        @NotNull DeploymentStatus status) {
}
