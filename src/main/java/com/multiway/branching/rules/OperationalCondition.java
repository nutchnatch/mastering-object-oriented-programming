package com.multiway.branching.rules;

import com.multiway.branching.states.DeviceStatus;
import com.multiway.branching.states.OperationalStatus;

import java.util.Optional;

public class OperationalCondition<T extends DeviceStatus> implements ExtendingCondition<T, T> {
    private OperationalStatus pattern;

    public OperationalCondition(OperationalStatus pattern) {
        this.pattern = pattern;
    }

    /**
     * OperationCondition matches the OperationalStatus stored in the status object
     * This is the replacement of the "if else instructions"
     * @param status
     * @return
     */
    @Override
    public Optional<T> applicableTo(T status) {
        return status.matches(this.pattern).map(s -> status);
    }
}
