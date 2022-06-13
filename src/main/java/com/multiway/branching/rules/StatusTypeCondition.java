package com.multiway.branching.rules;

import com.multiway.branching.states.DeviceStatus;

import java.util.Optional;

public class StatusTypeCondition<T extends DeviceStatus> implements RootCondition<T> {
    private Class<T> statusType;

    public StatusTypeCondition(Class<T> statusType) {
        this.statusType = statusType;
    }

    /**
     * Verifies if the statusType is of type T
     * If successful, it casts the statusType object down to a concrete subtype
     * @param status
     * @return
     */
    @Override
    public Optional<T> applicableTo(DeviceStatus status) {
        return this.statusType.isAssignableFrom(statusType.getClass())
                ? Optional.of((T)status)
                : Optional.empty();
    }
}
