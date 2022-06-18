package com.multiway.branching.builders;

import com.multiway.branching.ClaimingRule;
import com.multiway.branching.states.DeviceStatus;
import com.multiway.branching.states.SensorFailedStatus;

import java.util.function.Consumer;

/**
 * Abstract builder
 * Accepts those three primitive actions
 * And offers a ClaimingRule
 */
public interface ClaimingRulesBuilder {
    ClaimingRulesBuilder onMoneyBack(Consumer<DeviceStatus> action);
    ClaimingRulesBuilder onClaimExpress(Consumer<DeviceStatus> action);
    ClaimingRulesBuilder onClaimExtend(Consumer<SensorFailedStatus> action);
    ClaimingRule build();
}
