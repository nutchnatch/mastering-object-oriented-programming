package com.multiway.branching.rules;

import com.multiway.branching.states.DeviceStatus;

import java.util.Optional;

/**
 * Extends the chain of types transform
 * Transform state into a another kind of device status through method applicableTo()
 * @param <T1>
 * @param <T2>
 */
public interface ExtendingCondition<T1 extends DeviceStatus, T2 extends DeviceStatus> {
    Optional<T2> applicableTo(T1 status);
}
