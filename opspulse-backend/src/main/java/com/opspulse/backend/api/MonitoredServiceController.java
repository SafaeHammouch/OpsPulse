package com.opspulse.backend.api;

import com.opspulse.backend.check.HealthCheckResult;
import com.opspulse.backend.check.HealthCheckResultRepository;
import com.opspulse.backend.check.HealthCheckService;
import com.opspulse.backend.service.MonitoredService;
import com.opspulse.backend.service.MonitoredServiceRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/services")
public class MonitoredServiceController {

    private final MonitoredServiceRepository monitoredServiceRepository;
    private final HealthCheckService healthCheckService;
    private final HealthCheckResultRepository healthCheckResultRepository;

    public MonitoredServiceController(MonitoredServiceRepository monitoredServiceRepository,
                                      HealthCheckService healthCheckService,
                                      HealthCheckResultRepository healthCheckResultRepository) {
        this.monitoredServiceRepository = monitoredServiceRepository;
        this.healthCheckService = healthCheckService;
        this.healthCheckResultRepository = healthCheckResultRepository;
    }

    @PostMapping
    public ResponseEntity<MonitoredServiceResponse> create(@Valid @RequestBody CreateMonitoredServiceRequest request) {
        MonitoredService saved = monitoredServiceRepository.save(new MonitoredService(
                request.name(), request.healthUrl(), request.criticality(), request.checkIntervalSeconds()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(MonitoredServiceResponse.from(saved));
    }

    @GetMapping
    public List<MonitoredServiceResponse> list() {
        return monitoredServiceRepository.findAll().stream().map(MonitoredServiceResponse::from).toList();
    }

    @PostMapping("/{id}/check")
    public HealthCheckResultResponse check(@PathVariable Long id) {
        HealthCheckResult result = healthCheckService.check(id);
        return HealthCheckResultResponse.from(result);
    }

    @GetMapping("/{id}/health-results")
    public List<HealthCheckResultResponse> healthResults(@PathVariable Long id) {
        if (!monitoredServiceRepository.existsById(id)) {
            throw new ServiceNotFoundException(id);
        }
        return healthCheckResultRepository.findByServiceIdOrderByCheckedAtDesc(id).stream()
                .map(HealthCheckResultResponse::from)
                .toList();
    }
}
