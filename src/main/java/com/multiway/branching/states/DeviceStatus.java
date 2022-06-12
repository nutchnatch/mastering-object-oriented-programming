package com.multiway.branching.states;

import java.time.LocalDate;
import java.util.Optional;

public class DeviceStatus {

    private final OperationalStatus state;

    public DeviceStatus(OperationalStatus state) {
        this.state = state;
    }

    public static DeviceStatus allFine() {
        return new DeviceStatus(OperationalStatus.allFine());
    }

    public static DeviceStatus notOperational() {
        return new DeviceStatus(OperationalStatus.notOperational());
    }

    public static DeviceStatus visiblyDamaged(){
        return new DeviceStatus(OperationalStatus.visiblyDamaged());
    }

    /**
     * It is not possible to start a sensor failed without passing a date
     * @param detectedOn
     * @return
     */
    public static DeviceStatus sensorFailed(LocalDate detectedOn) {
        return new SensorFailedStatus(detectedOn, OperationalStatus.allFine());
    }

    public DeviceStatus andNotOperational() {
        return new DeviceStatus(this.state.andNotOperational());
    }

    public DeviceStatus andVisiblyDamaged(){
        return new DeviceStatus(this.state.andVisiblyDamaged());
    }

    public DeviceStatus andSensorFailed(LocalDate detectedOn){
        return new SensorFailedStatus(detectedOn, this.state);
    }

    public Optional<DeviceStatus> matches(OperationalStatus pattern) {
        return this.state.equals(pattern)
                ? Optional.of(this)
                : Optional.empty();
    }

    @Override
    public String toString() {
        return this.state.toString();
    }
}
