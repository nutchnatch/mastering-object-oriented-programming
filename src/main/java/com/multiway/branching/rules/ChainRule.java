package com.multiway.branching.rules;

import com.multiway.branching.Action;
import com.multiway.branching.ClaimingRule;
import com.multiway.branching.states.OperationalStatus;

import java.util.Optional;

/**
 * This class is not part of the domain
 * We can consider, packaging technical classes together with abstractions
 */
public class ChainRule implements ClaimingRule {
    private ClaimingRule head;
    private ClaimingRule tail;

    public ChainRule(ClaimingRule head, ClaimingRule tail) {
        this.head = head;
        this.tail = tail;
    }

    /**
     * There are 3 possibilities.
     * 1- head returns an action, which will be wrapped into an action and returned
     * 2- tail could return an non-empty optional action
     * 3- or if tail is filtered out, will return an empty optional
     * This approach is recursive, imagining that tail is itself a chained rule, making an infinite chain os rules
     * @param status
     * @return
     */
    @Override
    public Optional<Action> applicableTo(OperationalStatus status) {
        return this.head
                .applicableTo(status)
                .map(Optional::of)
                .orElse(tail.applicableTo(status));
    }
}
