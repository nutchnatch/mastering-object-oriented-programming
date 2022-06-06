package com.composite;

import com.domain.logic.with.streams.Money;
import com.domain.logic.with.streams.Painter;

import java.time.Duration;
import java.util.Optional;

/**
 * Time proportional to area
 * Compensation proportional to time
 */
public class ProportionalPainter implements Painter {

    private String name;
    private double sqMetersPerHour;
    private MoneyRate rate;

    public ProportionalPainter(String name, double sqMetersPerHour, MoneyRate rate) {
        this.name = name;
        this.sqMetersPerHour = sqMetersPerHour;
        this.rate = rate;
    }

    @Override
    public String getName() { return this.name; }

    @Override
    public double estimateSqMeters(Duration time) {
        return this.sqMetersPerHour * (time.getSeconds()) / (double)3600;
    }

    private int getSecondsToPaint(double sqMeters) {
        return (int)(sqMeters / this.sqMetersPerHour * 3600);
    }

    @Override
    public Optional<Painter> available() {
        return Optional.of(this);
    }

    @Override
    public Duration estimateTimeToPaint(double sqMeters) {
        return Duration.ofSeconds(this.getSecondsToPaint(sqMeters));
    }

    @Override
    public Money estimateCompensation(double sqMeters) {
        return this.rate.getTotalFor(this.estimateTimeToPaint(sqMeters));
    }

    public String toString() {
        return String.format(
                "%s painting %.2f sq. meters per hour at rate %s",
                this.getName(), this.sqMetersPerHour, this.rate);
    }

}
