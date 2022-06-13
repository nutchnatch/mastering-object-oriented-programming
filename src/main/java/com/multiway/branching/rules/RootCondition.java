package com.multiway.branching.rules;

import com.multiway.branching.ClaimingRule;
import com.multiway.branching.states.DeviceStatus;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Chain of conditions
 * Separate the rule conditions from the rule actions
 * Check one status object against several conditions
 * And only produce an action if all tests pass
 * Access components of concrete type T
 * This is the way to dig through concrete state components at runtime
 * It is called RootCondition because, it translates the root type (DeviceStatus) into something more derived
 */
public interface RootCondition<T extends DeviceStatus> {
    Optional<T> applicableTo(DeviceStatus status);

    /**
     * Any concrete status that has passed the condition check, will be consumed by a strong retyped action
     * Stored that knowledge in the object of class RuleFixture
     * It fixes the chain of condition together with an action
     * @param action
     * @return
     */
    default ClaimingRule applies(Consumer<T> action) {
        return new RuleFixture<>(this, action);
    }
}
