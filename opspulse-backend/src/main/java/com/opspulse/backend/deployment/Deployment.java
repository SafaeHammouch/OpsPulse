package com.opspulse.backend.deployment;

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
@Table(name = "deployments")
public class Deployment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private MonitoredService service;

    @Column(nullable = false, length = 120)
    private String version;

    @Column(nullable = false, length = 60)
    private String environment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeploymentStatus status;

    @Column(nullable = false)
    private Instant deployedAt = Instant.now();

    protected Deployment() { }

    public Deployment(MonitoredService service, String version, String environment, DeploymentStatus status) {
        this.service = service;
        this.version = version;
        this.environment = environment;
        this.status = status;
    }

    public Long getId() { return id; }
    public MonitoredService getService() { return service; }
    public String getVersion() { return version; }
    public String getEnvironment() { return environment; }
    public DeploymentStatus getStatus() { return status; }
    public Instant getDeployedAt() { return deployedAt; }
}
