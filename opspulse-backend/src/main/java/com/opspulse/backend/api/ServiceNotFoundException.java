package com.opspulse.backend.api;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(Long serviceId) {
        super("No monitored service exists with id " + serviceId);
    }
}
