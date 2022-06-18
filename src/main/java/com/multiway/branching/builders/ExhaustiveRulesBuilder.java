package com.multiway.branching.builders;

import com.multiway.branching.ClaimingRule;
import com.multiway.branching.rules.State;
import com.multiway.branching.states.DeviceStatus;
import com.multiway.branching.states.OperationalStatus;
import com.multiway.branching.states.SensorFailedStatus;

import java.util.function.Consumer;

/**
 * Exhausts all combinations of state
 */
public class ExhaustiveRulesBuilder implements ClaimingRulesBuilder {
    private Consumer<DeviceStatus> moneyBackAction = s -> {};
    private Consumer<DeviceStatus> expressAction = s -> {};
    private Consumer<SensorFailedStatus> extendedAction = s -> {};

    @Override
    public ClaimingRulesBuilder onMoneyBack(Consumer<DeviceStatus> action) {
        this.moneyBackAction = action;
        return this;
    }

    @Override
    public ClaimingRulesBuilder onClaimExpress(Consumer<DeviceStatus> action) {
        this.expressAction = action;
        return this;
    }

    @Override
    public ClaimingRulesBuilder onClaimExtend(Consumer<SensorFailedStatus> action) {
        this.extendedAction = action;
        return this;
    }

    @Override
    public ClaimingRule build() {
        return this.allFineRule()
                .orElse(this.notOperationalRule())
                .orElse(this.visibleDamageRule())
                .orElse(this.sensorFailedRule())
                .orElse(this.notOperationalDamageRule())
                .orElse(this.notOperationalSensorFailedRule())
                .orElse(this.visibilityDamagedSensorFailedRule())
                .orElse(this.notOperationalDamagedSensorFailedRule());
    }

    private ClaimingRule allFineRule() {
        return State
                .matching(OperationalStatus.allFine())
                .applies(this::moneyBack);
    }

    /**
     * Domain Specific Language for build warranty claiming rules
     * @return
     */
    private ClaimingRule notOperationalRule() {
        return State
                .matching(OperationalStatus.notOperational())
                .applies(s -> {
                    this.moneyBack(s);
                    this.express(s);
                });
    }

    private ClaimingRule visibleDamageRule() {
        return State
                .matching(OperationalStatus.visiblyDamaged())
                .applies(this::doNothing);
    }

    private ClaimingRule sensorFailedRule() {
        return State
                .matching(OperationalStatus.sensorFailed(), SensorFailedStatus.class)
                .applies(
                        s -> {
                            this.moneyBack(s);
                            this.extended(s);
                        }
                );
    }

    private ClaimingRule notOperationalDamageRule() {
        return State
                .matching(OperationalStatus.notOperational().andVisiblyDamaged())
                .applies(this::express);
    }

    private ClaimingRule notOperationalSensorFailedRule() {
        return State
                .matching(OperationalStatus.notOperational().andSensorFailed(), SensorFailedStatus.class)
                .applies(
                        s -> {
                            this.moneyBack(s);
                            this.express(s);
                            this.extended(s);
                        }
                );
    }

    private ClaimingRule visibilityDamagedSensorFailedRule() {
        return State
                .matching(OperationalStatus.visiblyDamaged().andSensorFailed(), SensorFailedStatus.class)
                .applies(this::extended);
    }

    private ClaimingRule notOperationalDamagedSensorFailedRule() {
        return State
                .matching(OperationalStatus.notOperational().andVisiblyDamaged().andSensorFailed(), SensorFailedStatus.class)
                .applies(
                        s -> {
                            this.express(s);
                            this.extended(s);
                        }
                );
    }

    private void moneyBack(DeviceStatus status) {
        this.moneyBackAction.accept(status);
    }

    private void express(DeviceStatus status) {
        this.expressAction.accept(status);
    }

    private void extended(SensorFailedStatus status) {
        this.extendedAction.accept(status);
    }

    private void doNothing(DeviceStatus status) {}
}
