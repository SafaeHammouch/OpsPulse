package com.opspulse.backend.check;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, Long> {
    List<HealthCheckResult> findByServiceIdOrderByCheckedAtDesc(Long serviceId);

    Optional<HealthCheckResult> findTopByServiceIdOrderByCheckedAtDesc(Long serviceId);
}
