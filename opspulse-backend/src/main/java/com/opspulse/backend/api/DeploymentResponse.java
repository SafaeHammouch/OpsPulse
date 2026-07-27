package com.opspulse.backend.api;

import com.opspulse.backend.deployment.Deployment;
import com.opspulse.backend.deployment.DeploymentStatus;
import java.time.Instant;

public record DeploymentResponse(Long id, Long serviceId, String serviceName, String version,
                                 String environment, DeploymentStatus status, Instant deployedAt) {
    public static DeploymentResponse from(Deployment deployment) {
        return new DeploymentResponse(deployment.getId(), deployment.getService().getId(),
                deployment.getService().getName(), deployment.getVersion(), deployment.getEnvironment(),
                deployment.getStatus(), deployment.getDeployedAt());
    }
}
