package com.opspulse.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point. @SpringBootApplication enables component scanning,
 * auto-configuration, and Spring Boot configuration support.
 */
@SpringBootApplication
public class DemoPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoPaymentServiceApplication.class, args);
    }
}
