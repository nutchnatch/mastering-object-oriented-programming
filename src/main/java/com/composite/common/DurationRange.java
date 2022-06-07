package com.composite.common;

import java.time.Duration;
import java.util.function.Function;

public class DurationRange {

    private Duration low;
    private Duration high;

    public DurationRange(Duration low, Duration high) {
        this.low = low;
        this.high = high;
    }

    public static DurationRange zeroTo(Duration high) {
        return new DurationRange(Duration.ZERO, high);
    }

    public Duration middle() {
        return this.low.plus(high).dividedBy(2);
    }

    public Duration range() {
        return this.high.minus(this.low);
    }

    public DurationRange lowerHalf() {
        return new DurationRange(this.low, this.middle());
    }

    public DurationRange upperHalf() {
        return new DurationRange(this.middle(), this.high);
    }

    public <TCriterion extends Comparable<TCriterion>> DurationBisection<TCriterion> bisect(
            Function<Duration, TCriterion> criterionFunction) {
        return new DurationBisection<TCriterion>(this, criterionFunction);
    }
}
