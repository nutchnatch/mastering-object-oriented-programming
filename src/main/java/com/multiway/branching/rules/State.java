package com.multiway.branching.rules;

import com.multiway.branching.states.DeviceStatus;
import com.multiway.branching.states.OperationalStatus;

/**
 * Rules are extensible
 * We can add more components to the DeviceState, and then write correspondent condition class that match them
 * Consuming code will never notice that anything was added to the project
 * That is the power of composition in Object Oriented Programming
 */
public class State {
    /**
     * Matches only an OperationStatus
     * @param status
     * @return
     */
    public static RootCondition<DeviceStatus> matching(OperationalStatus status) {
        return matching(status, DeviceStatus.class);
    }

    public static RootCondition<DeviceStatus> doesNotInclude(OperationalStatus status) {
        return new InvertingCondition(
                new AppendingCondition<>(
                        new StatusTypeCondition<>(DeviceStatus.class),
                        new OperationalMaskCondition<>(status)
                )
        );
    }

    public static <T extends DeviceStatus> RootCondition<T> matching(Class<T> stateType) {
        return new StatusTypeCondition<>(stateType);
    }

    public static RootCondition<DeviceStatus> includes(OperationalStatus status) {
        return new AppendingCondition<>(
                        new StatusTypeCondition<>(DeviceStatus.class),
                        new OperationalMaskCondition<>(status)
                );
    }

    /**
     * Matches both Matches only an OperationStatus and runtime type
     * @param pattern
     * @param stateType
     * @param <T>
     * @return
     */
    public static <T extends DeviceStatus> RootCondition<T> matching(OperationalStatus pattern, Class<T> stateType) {
        return new AppendingCondition<>(new StatusTypeCondition<>(stateType), new OperationalCondition<>(pattern));
    }
}
