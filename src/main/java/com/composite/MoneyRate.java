package com.composite;

import com.domain.logic.with.streams.Money;

import java.time.Duration;

public class MoneyRate {
    private Money intervalAmount;
    private Duration interval;

    public Duration getInterval() {
        return interval;
    }

    private long getSeconds() {
        return this.getInterval().getSeconds();
    }

    private MoneyRate(Money intervalAmount, Duration interval) {
        this.intervalAmount = intervalAmount;
        this.interval = interval;
    }

    public static MoneyRate hourly(Money amount) {
        return new MoneyRate(amount, Duration.ofHours(1));
    }

    private Money getTotalPerHour() {
        return this.getTotalFor(Duration.ofHours(1));
    }

    public Money getTotalFor(Duration interval) {
        return this.intervalAmount.scale(interval.getSeconds(), this.getSeconds());
    }

    @Override
    public String toString() {
        return this.getTotalPerHour() + "/hr.";
    }
}
