package com.composite;

import com.domain.logic.with.streams.Money;
import com.domain.logic.with.streams.Painter;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Optional;


/**
 * Aggregates information of many painters
 * Composite class map a single call to call contained objects
 * Collects multiple results into a single result
 * It is represented as a single object, with many objects behind
 */
public class CompositePainter implements Painter {
    private List<Painter> painters;

    /**
     * A CompositePainter should never be created without a list of painters
     * Should not be allowed
     * CompositePainter should not be public, but private
     * The caller should only get an Optional CompositePainter
     * @param painters
     */
//    public CompositePainter(List<Painter> painters) {
    private CompositePainter(List<Painter> painters) {
        this.painters = painters;
    }

    public static Optional<CompositePainter> of(List<Painter> painters) {
        return painters.isEmpty()
                ? Optional.empty()
                : Optional.of(new CompositePainter(painters));

    }

    /**
     * Returning a boolean is a problem, since this method is telling the caller how to implement a varying feature.
     * It should offer a polymorphic service instead (for ex, available)
     * @return
     */
//    @Override
//    public boolean isAvailable() {
//        return Painter.stream(this.painters).anyMatch(Painter::isAvailable);
//    }

    @Override
    public Optional<Painter> available() {
        return CompositePainter.of(
            Painter.stream(this.painters).available().collect(Collectors.toList())
        ).map(Function.identity());
    }

    @Override
    public Duration estimateTimeToPaint(double sqMeters) {
        return this.estimateTimeToPaint(sqMeters, this.estimateTotalVelocity(sqMeters));
    }

    /**
     * This method should be deterministic, as supposed to be, should not return Optional
     * Because we know that CompositePainter is not create with empty list of painters
     * @param sqMeters
     * @param totalVelocity
     * @return
     */
//    private Optional<Duration> estimateTimeToPaint(double sqMeters, Velocity totalVelocity) {
    private Duration estimateTimeToPaint(double sqMeters, Velocity totalVelocity) {
        return  Painter.stream(this.painters)
                .map(painter -> painter.estimateTimeToPaint(
                    sqMeters * painter.estimateVelocity(sqMeters).divideBy(totalVelocity)))
                .max(Duration::compareTo)
                .get();
    }

    @Override
    public Money estimateCompensation(double sqMeters) {
        return this.estimateCompensation(sqMeters, this.estimateTotalVelocity(sqMeters));
    }

    private Money estimateCompensation(double sqMeters, Velocity totalVelocity) {
        return Painter.stream(this.painters)
                .map(painter -> painter.estimateCompensation(
                        sqMeters * painter.estimateVelocity(sqMeters).divideBy(totalVelocity)))
                .reduce(Money::add)
                .orElse(Money.ZERO);
    }

    private Velocity estimateTotalVelocity(double sqMeters) {
        return Painter.stream(painters)
                        .map(painter -> painter.estimateVelocity(sqMeters))
                        .reduce(Velocity::add)
                        .orElse(Velocity.ZERO);
    }

    @Override
    public String getName() {
        return this.getPainterNames()
                .collect(Collectors.joining(", ", "{ ", " }"));
    }

    private Stream<String> getPainterNames() {
        return Painter.stream(this.painters).map(Painter::getName);
    }
}
