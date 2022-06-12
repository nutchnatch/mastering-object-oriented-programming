package com.multiway.branching;

import com.multiway.branching.rules.ChainRule;
import com.multiway.branching.states.OperationalStatus;

import java.util.Optional;

/**
 * This interface will test de DeviceStatus for equality
 * Has to take into account the sensorFailureDate parameter
 * Tells whenever it is applicable or not, given the DeviceStatus
 */
public interface ClaimingRule {

    // Classic design rule - expose a boolean method which test applicability
//    boolean isApplicableTo(DeviceStatus status);
    // The consumer will find the rule which finds true and apply
//    void apply();

    // The above classic design is not Object Oriented
    // Boolean methods are a limitation
    // Better design follows
    // This is a filtering form that can transform this interface
    Optional<Action> applicableTo(OperationalStatus status);

    /**
     * Procedure to introduce a technical detail to the code base without inflicting complexity on any consuming code
     * We can leverage the default implementation and save concrete rules to bother with this isolate concept
     * @param next
     * @return
     */
    default ClaimingRule orElse(ClaimingRule next) {
        return new ChainRule(this, next);
    }


}
