package com.opspulse.backend.check;

import com.opspulse.backend.service.MonitoredService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "health_check_results")
public class HealthCheckResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private MonitoredService service;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HealthStatus status;

    private Integer httpStatus;
    private long responseTimeMs;

    @Column(length = 2_000)
    private String errorMessage;

    @Column(nullable = false)
    private Instant checkedAt = Instant.now();

    protected HealthCheckResult() {
    }

    public HealthCheckResult(MonitoredService service, HealthStatus status, Integer httpStatus,
                             long responseTimeMs, String errorMessage) {
        this.service = service;
        this.status = status;
        this.httpStatus = httpStatus;
        this.responseTimeMs = responseTimeMs;
        this.errorMessage = errorMessage;
    }

    public Long getId() { return id; }
    public MonitoredService getService() { return service; }
    public HealthStatus getStatus() { return status; }
    public Integer getHttpStatus() { return httpStatus; }
    public long getResponseTimeMs() { return responseTimeMs; }
    public String getErrorMessage() { return errorMessage; }
    public Instant getCheckedAt() { return checkedAt; }
}
