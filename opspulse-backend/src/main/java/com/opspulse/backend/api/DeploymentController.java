package com.opspulse.backend.api;

import com.opspulse.backend.deployment.Deployment;
import com.opspulse.backend.deployment.DeploymentRepository;
import com.opspulse.backend.service.MonitoredService;
import com.opspulse.backend.service.MonitoredServiceRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/deployments")
public class DeploymentController {
    private final DeploymentRepository deploymentRepository;
    private final MonitoredServiceRepository monitoredServiceRepository;

    public DeploymentController(DeploymentRepository deploymentRepository,
                                MonitoredServiceRepository monitoredServiceRepository) {
        this.deploymentRepository = deploymentRepository;
        this.monitoredServiceRepository = monitoredServiceRepository;
    }

    @PostMapping
    public ResponseEntity<DeploymentResponse> create(@Valid @RequestBody CreateDeploymentRequest request) {
        MonitoredService service = monitoredServiceRepository.findById(request.serviceId())
                .orElseThrow(() -> new ServiceNotFoundException(request.serviceId()));
        Deployment saved = deploymentRepository.save(new Deployment(service, request.version(),
                request.environment(), request.status()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(DeploymentResponse.from(saved));
    }

    @GetMapping
    public List<DeploymentResponse> list(@RequestParam(required = false) Long serviceId) {
        List<Deployment> deployments = serviceId == null ? deploymentRepository.findAll()
                : deploymentRepository.findByServiceIdOrderByDeployedAtDesc(serviceId);
        return deployments.stream().map(DeploymentResponse::from).toList();
    }
}
