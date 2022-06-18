package com.multiway.branching.rules;

import com.multiway.branching.states.DeviceStatus;
import com.multiway.branching.states.OperationalStatus;

import java.util.Optional;

public class OperationalMaskCondition<T extends DeviceStatus> implements ExtendingCondition<T, T> {
    private OperationalStatus pattern;

    public OperationalMaskCondition(OperationalStatus pattern) {
        this.pattern = pattern;
    }

    @Override
    public Optional<T> applicableTo(T status) {
        return status.isSupersetOf(this.pattern).map(s -> status);
    }
}
