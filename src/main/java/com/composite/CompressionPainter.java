package com.composite;

import com.domain.logic.with.streams.Money;
import com.domain.logic.with.streams.Painter;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.util.Optional;

public class CompressionPainter implements Painter {

    private final String name;
    private final Duration fillTime;
    private final double fillAfterSqMeters;
    private final Duration cleaningTime;
    private final double sqMetersPerHour;
    private final MoneyRate rate;

    public CompressionPainter(String name, Duration fillTime, double fillAfterSqMeters, Duration cleaningTime, double sqMetersPerHour, MoneyRate rate) {
        this.name = name;
        this.fillTime = fillTime;
        this.fillAfterSqMeters = fillAfterSqMeters;
        this.cleaningTime = cleaningTime;
        this.sqMetersPerHour = sqMetersPerHour;
        this.rate = rate;
    }

    public long getCleaningSeconds() {
        return cleaningTime.getSeconds();
    }

    public long getFillSeconds() {
        return fillTime.getSeconds();
    }

    @Override
    public Optional<Painter> available() {
        return Optional.of(this);
    }

    @Override
    public Duration estimateTimeToPaint(double sqMeters) {
        Duration effectivePainting = Duration.ofSeconds((int)(sqMeters / this.sqMetersPerHour * 3600));
        int refillCount = (int)Math.ceil(sqMeters / this.fillAfterSqMeters);
        Duration refillTime = this.fillTime.multipliedBy(refillCount);
        return effectivePainting.plus(refillTime).plus(this.cleaningTime);
    }

    @Override
    public Money estimateCompensation(double sqMeters) {
        return this.rate.getTotalFor(estimateTimeToPaint(sqMeters));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double estimateSqMeters(Duration time) {
        long cleaningSeconds = this.getCleaningSeconds();
        long remainingSeconds = Math.max(time.getSeconds() - cleaningSeconds, 0);
        double burstSeconds = 3600 * this.fillAfterSqMeters / this.sqMetersPerHour;
        double fullBurstSeconds = burstSeconds + this.getFillSeconds();
        double fullBurst = (long)(remainingSeconds / fullBurstSeconds);
        double lastBurstSeconds = remainingSeconds - fullBurst * fullBurstSeconds;
        double lastBurstPaintingSeconds = Math.max(lastBurstSeconds - getFillSeconds(), 0);
        double totalPaintingSeconds = fullBurstSeconds * burstSeconds + lastBurstPaintingSeconds;

        return this.sqMetersPerHour * totalPaintingSeconds / 3600;
    }
}
