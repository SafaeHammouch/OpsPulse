package com.opspulse.backend.deployment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {
    List<Deployment> findByServiceIdOrderByDeployedAtDesc(Long serviceId);
}
