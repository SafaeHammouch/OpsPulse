package com.opspulse.backend.service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "monitored_services")
public class MonitoredService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 2_000)
    private String healthUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Criticality criticality;

    @Column(nullable = false)
    private int checkIntervalSeconds;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected MonitoredService() {
    }

    public MonitoredService(String name, String healthUrl, Criticality criticality, int checkIntervalSeconds) {
        this.name = name;
        this.healthUrl = healthUrl;
        this.criticality = criticality;
        this.checkIntervalSeconds = checkIntervalSeconds;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getHealthUrl() { return healthUrl; }
    public Criticality getCriticality() { return criticality; }
    public int getCheckIntervalSeconds() { return checkIntervalSeconds; }
    public boolean isEnabled() { return enabled; }
    public Instant getCreatedAt() { return createdAt; }
}
