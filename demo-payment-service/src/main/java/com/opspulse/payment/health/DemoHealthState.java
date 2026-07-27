package com.opspulse.payment.health;

import org.springframework.stereotype.Component;

/**
 * A singleton Spring component that holds the current demo state in memory.
 * This is deliberately simple: restarting the application resets it to HEALTHY.
 */
@Component
public class DemoHealthState {

    private volatile DemoHealthMode mode = DemoHealthMode.HEALTHY;

    public DemoHealthMode getMode() {
        return mode;
    }

    public void setMode(DemoHealthMode mode) {
        this.mode = mode;
    }
}
