package com.multiway.branching.states;

import java.time.LocalDate;

public class SensorFailedStatus extends DeviceStatus {
    private final LocalDate failureDetectedOn;

    public SensorFailedStatus(LocalDate detectedOn, OperationalStatus inState) {
        super(inState.andSensorFailed());
        this.failureDetectedOn = detectedOn;
    }

    public LocalDate getFailureDetectedDate() {
        return failureDetectedOn;
    }

    @Override
    public DeviceStatus andNotOperational() {
        return super.andNotOperational().andSensorFailed(this.failureDetectedOn);
    }

    @Override
    public DeviceStatus andVisiblyDamaged() {
        return super.andVisiblyDamaged().andSensorFailed(this.failureDetectedOn);
    }

    @Override
    public String toString() {
        return super.toString() + "Sensor failure detected on " + failureDetectedOn.toString();
    }
}
