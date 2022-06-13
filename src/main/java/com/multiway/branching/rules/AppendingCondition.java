package com.multiway.branching.rules;

import com.multiway.branching.states.DeviceStatus;

import java.util.Optional;

/**
 * Logical and operator
 * Joins RootCondition and ExtendingCondition into a larger RootCondition
 * Starts from a DeviceStatus, filter it to T1, and join another filter from T1 to T2
 * @param <T>
 * @param <T1>
 */
public class AppendingCondition<T extends DeviceStatus, T1 extends DeviceStatus> implements RootCondition<T1> {
    private RootCondition<T> first;
    private ExtendingCondition<T, T1> second;

    public AppendingCondition(RootCondition<T> first, ExtendingCondition<T, T1> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Optional<T1> applicableTo(DeviceStatus status) {
        return this.first.applicableTo(status)
                .map(this.second::applicableTo)
                .orElse(Optional.empty());
    }
}
