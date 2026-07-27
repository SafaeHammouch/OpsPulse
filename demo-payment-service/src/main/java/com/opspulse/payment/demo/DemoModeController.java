package com.opspulse.payment.demo;

import com.opspulse.payment.health.DemoHealthMode;
import com.opspulse.payment.health.DemoHealthState;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** HTTP controls used only to demonstrate the monitored service's behavior. */
@RestController
@RequestMapping("/demo")
public class DemoModeController {

    private final DemoHealthState healthState;

    public DemoModeController(DemoHealthState healthState) {
        this.healthState = healthState;
    }

    @PostMapping("/healthy")
    public ResponseEntity<Map<String, String>> setHealthy() {
        return setMode(DemoHealthMode.HEALTHY);
    }

    @PostMapping("/slow")
    public ResponseEntity<Map<String, String>> setSlow() {
        return setMode(DemoHealthMode.SLOW);
    }

    @PostMapping("/down")
    public ResponseEntity<Map<String, String>> setDown() {
        return setMode(DemoHealthMode.DOWN);
    }

    private ResponseEntity<Map<String, String>> setMode(DemoHealthMode mode) {
        healthState.setMode(mode);
        return ResponseEntity.ok(Map.of("mode", mode.name()));
    }
}
