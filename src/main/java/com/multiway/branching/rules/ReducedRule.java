package com.multiway.branching.rules;

import com.multiway.branching.Action;
import com.multiway.branching.states.DeviceStatus;

import java.util.function.Consumer;

/**
 * Strongly typed argument
 * Where all those moving parts will be reduced to a single action
 * @param <T>
 */
public class ReducedRule<T extends DeviceStatus> implements Action {
    private T status;
    private Consumer<T> action;

    public ReducedRule(T status, Consumer<T> action) {
        this.status = status;
        this.action = action;
    }

    /**
     * Status will be passed to the action without exposing any of those to the public
     */
    @Override
    public void apply() {
        this.action.accept(this.status);
    }
}
