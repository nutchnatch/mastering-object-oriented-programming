package com.composite;

import com.domain.logic.with.streams.OptionalPainter;
import com.domain.logic.with.streams.Painter;
import com.domain.logic.with.streams.PaintersStream;
import com.domain.logic.with.streams.WorkStream;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Domain specific language, so that our code speaks to human experts in the field of painting houses
 * A detailed domain model can lead to a unification of ideas and processes
 * Pushing that principal to its natural extreme, brings us to the development of a Main Specific Language
 * Internal DSL's are developed in the language in which the rest of model is programmed
 * The primary users are the programmers
 * DSL lets programmers write more effective code, chaining meaningful calls and using meaningful intermediate objects
 * Opposed to traditional coding styles, DSL based style is more flexible and very expressive
 * Code is intention-revealed by design
 * Preconditions of a DSL:
 *  - Objects and methods must be composable
 *  - Composable method is small and isolated operation which is only performing one transformation
 *  It returns an object which then exposes the next operation in a chain
 *  By chaining several atomic transformations, we can construct behavior of immense complexity
 *  Implementing composability, composable methods will not modify the objects on which it executes
 *  Rather, it constructs and return a new object which reflects the state modification
 *  DSL goes naturally hands-in-hands with immutability
 */
public class SelectingScheduler implements PaintingScheduler {
    private Function<Double, Comparator<Painter>> minByFactory;

    public SelectingScheduler(Function<Double, Comparator<Painter>> minByFactory) {
        this.minByFactory = minByFactory;
    }

    @Override
    public WorkStream schedule(List<Painter> painters, double sqMeters) {
        return this.getWinner(painters, sqMeters).assign(sqMeters).stream();
    }

    public static PaintingScheduler cheapest() {
        return new SelectingScheduler(SelectingScheduler::cheaper);
    }

    public static PaintingScheduler fastest() {
        return new SelectingScheduler(SelectingScheduler::faster);
    }

    private static  Comparator<Painter> cheaper(double sqMeters) {
        return Comparator.comparing(painter -> painter.estimateTimeToPaint(sqMeters));
    }

    private static Comparator<Painter> faster(double sqMeters) {
        return Comparator.comparing(painter -> painter.estimateCompensation(sqMeters));
    }

    private OptionalPainter getWinner(List<Painter> painters, double sqMeters) {
        return this.getWinner(painters, this.getComparator(sqMeters));
    }

    private OptionalPainter getWinner(List<Painter> painters, Comparator<Painter> minBy) {
        return this.available(painters)
                .min(minBy)
                .map(OptionalPainter::of)
                .orElse(OptionalPainter.empty());

    }

    private PaintersStream available(List<Painter> painters) {
        return Painter.stream(painters).available();
    }

    private Comparator<Painter> getComparator(double sqMeters) {
        return this.minByFactory.apply(sqMeters);
    }
}
