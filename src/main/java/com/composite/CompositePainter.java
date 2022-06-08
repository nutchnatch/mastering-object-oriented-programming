package com.composite;

import com.domain.logic.with.streams.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Optional;


/**
 * Aggregates information of many painters
 * Composite class map a single call to call contained objects
 * Collects multiple results into a single result
 * It is represented as a single object, with many objects behind
 * An algorithm that must vary, should be represented by an object
 *
 * Methods have been transformed in order to use Domain Specific Language
 * The code produced by Domain Specific Language, is self evident
 * Imperative code contributes to hide programmer's intention
 * To develop intention revealing code and ultimately the Domain Specific Language,
 * We have to replace explicit controls (if, for, collect, etc) structure with declarative calls and give a meaningful name to each call
 * Reach or Deap Model Object - every element in our thinking has a corresponding programmatic type which nodels it
 */
public class CompositePainter implements Painter {
    private List<Painter> subordinatePainters;
    private PaintingScheduler scheduler;

    /**
     * A CompositePainter should never be created without a list of painters
     * Should not be allowed
     * CompositePainter should not be public, but private
     * The caller should only get an Optional CompositePainter
     * @param subordinatePainters
     */
//    public CompositePainter(List<Painter> painters) {
    private CompositePainter(List<Painter> subordinatePainters, PaintingScheduler scheduler) {
        this.subordinatePainters = subordinatePainters;
        this.scheduler = scheduler;
    }

    public static OptionalPainter of(List<Painter> subordinatePainters, PaintingScheduler scheduler) {
        return subordinatePainters.isEmpty()
                ? OptionalPainter.empty()
                : OptionalPainter.of(new CompositePainter(subordinatePainters, scheduler));

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
    public OptionalPainter available() {
        // This code can be improves
//        return CompositePainter.of(
//            painters().available().collect(Collectors.toList()), this.scheduler
//        ).map(Function.identity());
        return painters().available().workTogether(this.scheduler);
    }

    /**
     * scheduler is the strategy on the strategy patter used on this solution
     * @param sqMeters
     * @return
     */
    @Override
    public Duration estimateTimeToPaint(double sqMeters) {
        return this.schedule(sqMeters)
//                .map(WorkAssignment::estimateTimeToPaint)
                .timesToPaint()
                // Transform this stream of duration into a DurationStream
//                .max(Duration::compareTo)
//                .get();
                .maxOfMany();
    }

//    @Override
//    public Duration estimateTimeToPaint(double sqMeters) {
//        return this.estimateTimeToPaint(sqMeters, this.estimateTotalVelocity(sqMeters));
//    }

    /**
     * This method should be deterministic, as supposed to be, should not return Optional
     * Because we know that CompositePainter is not create with empty list of painters
     * @param sqMeters
//     * @param totalVelocity
     * @return
     */
//    private Optional<Duration> estimateTimeToPaint(double sqMeters, Velocity totalVelocity) {
//    private Duration estimateTimeToPaint(double sqMeters, Velocity totalVelocity) {
//        return  Painter.stream(this.painters)
//                // This algorithm is applicable if we presume constant velocity
//                // Should be substituted when the assumptions change
//                // The process of splitting the area into chunks would have to varry
//                .map(painter -> painter.estimateTimeToPaint(
//                    sqMeters * painter.estimateVelocity(sqMeters).divideBy(totalVelocity)))
//                .max(Duration::compareTo)
//                .get();
//    }

    /**
     * This method could be turned into an abstracted one
     * However, it risks combinatorial explosion of features
     * @param sqMeters
     * @return
     */
//    private Stream<WorkAssignment> schedule(double sqMeters) {
//        return this.scheduler.schedule(sqMeters, this.estimateTotalVelocity(sqMeters));
//    }

    /**
     * Cuts the area into sections proportional to individual velocity
     * @param sqMeters
     * @param totalVelocity
     */
//    private Stream<WorkAssignment> schedule(double sqMeters, Velocity totalVelocity) {
//        return  Painter.stream(this.painters)
//                .map(painter -> painter.assign(sqMeters * painter.estimateVelocity(sqMeters).divideBy(totalVelocity)));
//    }

    /**
     * Calculates the total velocity
     * @param sqMeters
     * @return
     */
//    private Velocity estimateTotalVelocity(double sqMeters) {
//        return Painter.stream(painters)
//                .map(painter -> painter.estimateVelocity(sqMeters))
//                .reduce(Velocity::add)
//                .orElse(Velocity.ZERO);
//    }

    /**
     * We can create a specialized stream for this case (of WorkAssignment)
     * @param sqMeters
     * @return
     */
    @Override
    public Money estimateCompensation(double sqMeters) {
        return this.schedule(sqMeters)
                // Mapping work assignment to a stream of many objects, means ask  for compensation
//                .map(WorkAssignment::estimateCompensation)
                .compensations()
                .sum();
    }

    private WorkStream schedule(double sqMeters) {
        return this.scheduler.schedule(this.subordinatePainters, sqMeters);
    }

//    @Override
//    public Money estimateCompensation(double sqMeters) {
//        return this.estimateCompensation(sqMeters, this.estimateTotalVelocity(sqMeters));
//    }
//
//    private Money estimateCompensation(double sqMeters, Velocity totalVelocity) {
//        return Painter.stream(this.painters)
//                .map(painter -> painter.estimateCompensation(
//                        sqMeters * painter.estimateVelocity(sqMeters).divideBy(totalVelocity)))
//                .reduce(Money::add)
//                .orElse(Money.ZERO);
//    }

    @Override
    public String getName() {
        return this.getPainterNames()
                .collect(Collectors.joining(", ", "{ ", " }"));
    }

    @Override
    public double estimateSqMeters(Duration time) {
//        return Painter.stream(painters) --> we can give this a name, since it is used a lot
        return painters().estimateSqMeters(time);
                    // Low level code belongs to low level class
//                .mapToDouble(painter -> painter.estimateSqMeters(time)) --> this can be replaced for a method with an expressive name
//                .sum();
    }

    private PaintersStream painters() {
        return Painter.stream(subordinatePainters);
    }

    private Stream<String> getPainterNames() {
        return painters().map(Painter::getName);
    }
}
