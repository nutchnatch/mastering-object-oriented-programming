package com.composite;

import com.composite.common.DurationRange;
import com.domain.logic.with.streams.Painter;
import com.domain.logic.with.streams.WorkStream;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;
import java.util.Optional;

/**
 * All painters should finish the work at the same time
 * The problem is that painters in the list might be of different types
 * Some might work in a non-linear wave
 * Most of the methods in a Domain Model can turned into one liners, once we reach the deap and detail Domain Model
 * Code which was hard to understand migrates to specialized classes each of which alone will be easier to understand
 */
public class EqualTimeScheduler implements PaintingScheduler {
    @Override
    public WorkStream schedule(List<Painter> painters, double sqMeters) {
        return this.getUpperDuration(painters, sqMeters)
                .map(upper -> scheduleNonEmpty(painters, sqMeters, upper))
                .orElse(WorkAssignment.stream(Stream.empty()));
    }

    private Optional<Duration> getUpperDuration(List<Painter> painters, double sqMeters) {
        return Painter.stream(painters).tomesToPaint(sqMeters).min();
//                .map(painter -> painter.estimateTimeToPaint(sqMeters))
//                .min(Duration::compareTo);
    }

    private WorkStream scheduleNonEmpty(List<Painter> painters, double sqMeters, Duration upper) {
        Duration totalTime = totalTime(painters, sqMeters, upper);

        return scheduleNonEmpty(painters, totalTime);
    }

    private WorkStream scheduleNonEmpty(List<Painter> painters, Duration totalTime) {
        return Painter.stream(painters).assign(totalTime);
    }

    private Duration totalTime(List<Painter> painters, double sqMeters, Duration upper) {
        return DurationRange.zeroTo(upper)
                .bisect(time -> this.totalSqMeters(painters, time))
                .convergeTo(sqMeters, Duration.ofMillis(1))
                .middle();
//        return totalTime(painters, sqMeters, upper, Duration.ZERO);
    }

//    private Duration totalTime(List<Painter> painters, double sqMeters, Duration upper, Duration lower) {
//        return upper.minus(lower).compareTo(Duration.ofMillis(1)) <= 0
//                ? lower
//                : totalTime(painters, sqMeters, upper, upper.plus(lower).dividedBy(2), lower);
//    }

    // This is a second domain, this class should not take this responsibility
    // This is more general than this scheduler
    // So, we can move it to another class - DurationRange
//    private Duration totalTime(List<Painter> painters, double sqMeters, Duration upper, Duration middle, Duration lower) {
//        return this.getTotalSqMeters(painters, middle) > sqMeters
//                ? this.totalTime(painters, sqMeters, middle, lower)
//                : this.totalTime(painters, sqMeters, upper, middle);
//    }

    private double totalSqMeters(List<Painter> painters, Duration time) {
        return Painter.stream(painters).estimateSqMeters(time);
//                .mapToDouble(painter -> painter.estimateSqMeters(time))
//                .sum();
    }
}
