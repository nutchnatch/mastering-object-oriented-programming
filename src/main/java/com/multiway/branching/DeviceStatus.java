package com.multiway.branching;

import java.util.Objects;

public class DeviceStatus {

    /**
     * Static final fields are not the right place to contain responsibilities
     */
//    public static final DeviceStatus ALL_FINE = new DeviceStatus(0);
    public static DeviceStatus allFine() { return new DeviceStatus(0);}
//    public static final DeviceStatus NOT_OPERATIONAL = new DeviceStatus(2);
    public static DeviceStatus notOperational() { return new DeviceStatus(2);}
//    public static final DeviceStatus VISIBLY_DAMAGED = new DeviceStatus(3);
    public static DeviceStatus visiblyDamaged(){ return new DeviceStatus(3);}
//    public static final DeviceStatus SENSOR_FAILED = new DeviceStatus(4);
    public static DeviceStatus sensorFailed() { return new DeviceStatus(4);}
//    public static final DeviceStatus NOT_OPERATIONAL_DAMAGE = combine(NOT_OPERATIONAL, VISIBLY_DAMAGED);
//    public static final DeviceStatus NOT_OPERATIONAL_SENSOR_FAILED = combine(NOT_OPERATIONAL, SENSOR_FAILED);
//    public static final DeviceStatus DAMAGED_SENSOR_FAILED = combine(VISIBLY_DAMAGED, SENSOR_FAILED);
//    public static final DeviceStatus NOT_OPERATIONAL_DAMAGE_SENSOR_FAILED = combine(NOT_OPERATIONAL, VISIBLY_DAMAGED);

    private final int representation;

    private DeviceStatus(int representation) {
        this.representation = representation;
    }

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

    public DeviceStatus add(DeviceStatus status) {
        return new DeviceStatus(this.representation | status.representation);
//        return Stream.of(
//                ALL_FINE, NOT_OPERATIONAL, VISIBLY_DAMAGED, SENSOR_FAILED, NOT_OPERATIONAL_DAMAGE, NOT_OPERATIONAL_SENSOR_FAILED,
//                DAMAGED_SENSOR_FAILED, NOT_OPERATIONAL_DAMAGE_SENSOR_FAILED)
//                .filter(val -> val.id == (this.id | status.id))
//                .findFirst()
//                .get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceStatus that = (DeviceStatus) o;
        return representation == that.representation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(representation);
    }
}
