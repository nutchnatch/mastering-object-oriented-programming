package com.composite.common;

import java.time.Duration;
import java.util.function.Function;

public class DurationBisection<TCriterion extends Comparable<TCriterion>> {
    private DurationRange initialRange;
    private Function<Duration, TCriterion> criterionFunction;

    public DurationBisection(DurationRange initialRange, Function<Duration, TCriterion> criterionFunction) {
        this.initialRange = initialRange;
        this.criterionFunction = criterionFunction;
    }

    public DurationRange convergeTo(TCriterion pivot, Duration tolerance) {
        DurationRange current = this.initialRange;

        while(current.range().compareTo(tolerance) > 0) {
            Comparable<TCriterion> criterion = this.criterionFunction.apply(current.middle());
            current = criterion.compareTo(pivot) >= 0 ? current.lowerHalf() : current.upperHalf();
        }

        return current;
    }
}
