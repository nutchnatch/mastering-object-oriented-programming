package com.multiway.branching.states;

import java.util.Objects;

public class OperationalStatus {
    /**
     * Static final fields are not the right place to contain responsibilities
     */
//    public static final DeviceStatus ALL_FINE = new DeviceStatus(0);
    public static OperationalStatus allFine() { return new OperationalStatus(0);}
//    public static final DeviceStatus NOT_OPERATIONAL = new DeviceStatus(2);
    public static OperationalStatus notOperational() { return new OperationalStatus(1);}
//    public static final DeviceStatus VISIBLY_DAMAGED = new DeviceStatus(3);
    public static OperationalStatus visiblyDamaged(){ return new OperationalStatus(2);}

    //    public static final DeviceStatus SENSOR_FAILED = new DeviceStatus(4);
    public static OperationalStatus sensorFailed() { return new OperationalStatus(4);}

    private final int representation;
    public OperationalStatus(int representation) {
        this.representation = representation;
    }

    public OperationalStatus andNotOperational() {
        return this.add(notOperational());
    }

    public OperationalStatus andVisiblyDamaged(){
        return this.add(visiblyDamaged());
    }

    public OperationalStatus andSensorFailed(){
        return this.add(sensorFailed());
    }

    public boolean isSupersetOf(OperationalStatus other) {
        return (this.representation & other.representation) == other.representation;
    }

//    public static final DeviceStatus SENSOR_FAILED = new DeviceStatus(4);
//    public static DeviceStatus sensorFailed(LocalDate detectedOn) { return new DeviceStatus(4);}
//    public static final DeviceStatus NOT_OPERATIONAL_DAMAGE = combine(NOT_OPERATIONAL, VISIBLY_DAMAGED);
//    public static final DeviceStatus NOT_OPERATIONAL_SENSOR_FAILED = combine(NOT_OPERATIONAL, SENSOR_FAILED);
//    public static final DeviceStatus DAMAGED_SENSOR_FAILED = combine(VISIBLY_DAMAGED, SENSOR_FAILED);
//    public static final DeviceStatus NOT_OPERATIONAL_DAMAGE_SENSOR_FAILED = combine(NOT_OPERATIONAL, VISIBLY_DAMAGED);

    /**
     * Enumerations are not allowed to construct a new object
     * Enumeration values are compared by reference, not by the value they contain
     * public DeviceStatus add(DeviceStatus status) {
     *      return new DeviceStatus(this.id | status.id)
     * }
     * @return
     */
//    public static DeviceStatus combine(DeviceStatus... statuses) {
//        return new DeviceStatus(Arrays.stream(statuses)
//                .mapToInt(status -> status.id)
//                .reduce(0, (a, b) -> a | b));
//    }

    public OperationalStatus add(OperationalStatus status) {
        return new OperationalStatus(this.representation | status.representation);
//        return Stream.of(
//                ALL_FINE, NOT_OPERATIONAL, VISIBLY_DAMAGED, SENSOR_FAILED, NOT_OPERATIONAL_DAMAGE, NOT_OPERATIONAL_SENSOR_FAILED,
//                DAMAGED_SENSOR_FAILED, NOT_OPERATIONAL_DAMAGE_SENSOR_FAILED)
//                .filter(val -> val.id == (this.id | status.id))
//                .findFirst()
//                .get();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof OperationalStatus && this.equals((OperationalStatus) o);
    }

    private boolean equals(OperationalStatus other) {
        return this.representation == other.representation;
    }


    @Override
    public int hashCode() {
        return this.representation;
    }

    @Override
    public String toString() {
        String result = "";
        String separator = "";
        if(this.isSupersetOf(notOperational())) {
            result += separator + "Not Operational";
            separator = " + ";
        }
        if(this.isSupersetOf(visiblyDamaged())) {
            result += separator + "Damaged";
            separator = " + ";
        }
        if(this.isSupersetOf(sensorFailed())) {
            result += separator + "Sensor Failed";
        }
        return result;
    }
}
