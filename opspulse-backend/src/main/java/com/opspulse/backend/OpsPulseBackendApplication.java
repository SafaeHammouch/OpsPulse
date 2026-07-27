package com.opspulse.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OpsPulseBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpsPulseBackendApplication.class, args);
    }
}
