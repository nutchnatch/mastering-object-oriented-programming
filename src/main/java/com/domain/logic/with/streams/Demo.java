package com.domain.logic.with.streams;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;

public class Demo {

    /**
     * This code is nor good since it introduces a gap between requirement and concrete implementation with three levels of nesting
     * Then, finding a bug if a hard task
     * The desired coding style would be: avoid explicit loops; let the sequence of elements perform the desired operation
     * This method has a design problem, it returns null if there is no painters. It should not return null to a caller.
     * Does not know if caller is expecting null. The return should be aligned to the method specification
     * @param sqMeters
     * @param painters
     * @return
     */
    private static Painter findCheapest1(double sqMeters, List<Painter> painters) {
        Money lowestCost = Money.ZERO;
        Painter winner = null;
        for(Painter candidate: painters) {
//            if(candidate.isAvailable()) {
                Money cost = candidate.estimateCompensation(sqMeters);
                if(winner == null || cost.compareTo(lowestCost) <= 0) {
                    winner = candidate;
                    lowestCost = cost;
                }
//            }
        }
        return winner;
    }

    /**
     * Replacing the method above with another approach
     * More readable: Available painter with minimum compensation estimate
     * Min - is an Aggregation Function -> pics a sequence of objects and return just one object
     * @param sqMeters
     * @param painters
     * @return
     */
    private static Optional<Painter> findCheapest2(double sqMeters, List<Painter> painters) {
        //promise that on object appears one after another. Then we tell the stream what do to with the object
        return painters.stream()
//                .filter(Painter::isAvailable)
//                .sorted(Comparator.comparing(painter -> painter.estimateCompensation(sqMeters)))  // same as min, but less performant (O(n) + logn)
//                .findFirst()
                .min(Comparator.comparing(painter -> painter.estimateCompensation(sqMeters))); // -> this is called Aggregation Function
//                .reduce((acc, current) -> acc.estimateCompensation(sqMeters)  // this is the reduce operation behind the min aggregation
//                        .compareTo(current.estimateCompensation(sqMeters)) <= 0 ? acc : current)
//                .get(); // Instead of return this values which may lead to a NPE, we can return an Optional
    }

    /**
     * As method findCheapest2 is not readable as expected, let's improve the readability
     * This online of code is doing the same as the method above
     * Now, it speaks the language of its business
     * @param sqMeters
     * @param painters
     * @return
     */
    private static Optional<Painter> findCheapest3(double sqMeters, List<Painter> painters) {
        return Painter.stream(painters).available().cheapest(sqMeters);
    }

    private static Money getTotalCost(double sqMeters, List<Painter> painters) {
        return painters.stream()
//                .filter(Painter::isAvailable)
                //Money.ZERO - start; (acc, painter) -> painter.estimateCompensation(sqMeters).add(acc) - aggregator function; Money::add - Acumulator
//                .reduce(Money.ZERO, (acc, painter) -> painter.estimateCompensation(sqMeters).add(acc), Money::add);
                .map(painter -> painter.estimateCompensation(sqMeters))
                .reduce(Money::add)
                .orElse(Money.ZERO);
    }

    public void run() {

    }
}
