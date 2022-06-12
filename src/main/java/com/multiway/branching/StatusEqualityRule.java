package com.multiway.branching;

import java.util.Optional;

public class StatusEqualityRule implements ClaimingRule, Action {
    private final DeviceStatus pattern;
    private final Runnable action;

    private StatusEqualityRule(DeviceStatus pattern, Runnable action) {
        this.pattern = pattern;
        this.action = action;
    }

    /**
     * This factory is to force the consumer to follow the legal path when creating a  StatusEqualityRule as a ClaimingRule
     * So, it cannot invoke the apply() method, without calling applicableTo() to verify if it is applicable
     * @param pattern
     * @param action
     * @return
     */
    public static ClaimingRule match(DeviceStatus pattern, Runnable action) {
        return new StatusEqualityRule(pattern, action);
    }

    /**
     * Can only be called after verifying if it is applicable through applicableTo() method
     */
    @Override
    public void apply() {
        this.action.run();
    }

    @Override
    public Optional<Action> applicableTo(DeviceStatus status) {
        return this.pattern.equals(status)
                ? Optional.of(this)
                : Optional.empty();
    }
}
