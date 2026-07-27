package com.opspulse.backend.check;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, Long> {
    List<HealthCheckResult> findByServiceIdOrderByCheckedAtDesc(Long serviceId);
}
