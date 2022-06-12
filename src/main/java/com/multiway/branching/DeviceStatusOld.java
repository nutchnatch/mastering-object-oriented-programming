package com.multiway.branching;

import java.util.Arrays;

/**
 * What if an object is in two states simultaneously (e.g NON_OPERATIONAL & SENSOR_FAILED)?
 * How many enum flags will be needed?
 * This will lead to a lot of combinations of existing flags
 * Adding two unique values, we can see a combinatorial explosion of cases
 * Enumeration shows a lack of Object Oriented design
 */
public enum DeviceStatusOld {

    ALL_FINE(0),
    NOT_OPERATIONAL(1),
    VISIBLY_DAMAGED(2),
    SENSOR_FAILED(3),
    NOT_OPERATIONAL_DAMAGE(NOT_OPERATIONAL.id | VISIBLY_DAMAGED.id),
    NOT_OPERATIONAL_SENSOR_FAILED(NOT_OPERATIONAL.id | SENSOR_FAILED.id),
    DAMAGED_SENSOR_FAILED(VISIBLY_DAMAGED.id | SENSOR_FAILED.id),
    NOT_OPERATIONAL_DAMAGE_SENSOR_FAILED(NOT_OPERATIONAL.id | VISIBLY_DAMAGED.id | SENSOR_FAILED.id);

    private final int id;

    private DeviceStatusOld(int id) {
        this.id = id;
    }

    /**
     * Enumerations are not allowed to construct a new object
     * Enumeration values are compared by reference, not by the value they contain
     * public DeviceStatus add(DeviceStatus status) {
     *      return new DeviceStatus(this.id | status.id)
     * }
     * @param status
     * @return
     */
    public DeviceStatusOld add(DeviceStatusOld status) {
        return Arrays.stream(DeviceStatusOld.class.getEnumConstants())
                .filter(val -> val.id == (this.id | status.id))
                .findFirst()
                .get();
    }
}
