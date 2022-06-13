package com.multiway.branching.rules;

import com.multiway.branching.Action;
import com.multiway.branching.ClaimingRule;
import com.multiway.branching.states.DeviceStatus;
import com.multiway.branching.states.OperationalStatus;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Fixes the chain of condition together with an action
 * This class is only implementing non-generic claim rule interface
 * Holds the chain of generic condition object plus the generic action
 * All those rules will remain there, without the exposure to consumers
 */
public class RuleFixture<T extends DeviceStatus> implements ClaimingRule {
    private RootCondition<T> condition;
    private Consumer<T> action;

    public RuleFixture(RootCondition<T> condition, Consumer<T> action) {
        this.condition = condition;
        this.action = action;
    }

    @Override
    public Optional<Action> applicableTo(DeviceStatus status) {
        return this.condition
                .applicableTo(status)
                .map(s -> new ReducedRule<>(s, this.action));
    }
}
