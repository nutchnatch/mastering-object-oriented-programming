package com.composite;

import com.domain.logic.with.streams.Painter;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;
import java.util.Optional;

/**
 * All painters should finish the work at the same time
 * The problem is that painters in the list might be of different types
 * Some might work in a non-linear wave
 */
public class EqualTimeScheduler implements PaintingScheduler {
    @Override
    public Stream<WorkAssignment> schedule(List<Painter> painters, double sqMeters) {
        return getUpperDuration(painters, sqMeters)
                .map(upper -> scheduleNonEmpty(painters, sqMeters, upper))
                .orElse(Stream.empty());
    }

    private Optional<Duration> getUpperDuration(List<Painter> painters, double sqMeters) {
        return Painter.stream(painters)
                .map(painter -> painter.estimateTimeToPaint(sqMeters))
                .min(Duration::compareTo);
    }

    private Stream<WorkAssignment> scheduleNonEmpty(List<Painter> painters, double sqMeters, Duration upper) {
        Duration totalTime = getTotalTime(painters, sqMeters, upper);

        return scheduleNonEmpty(painters, totalTime);
    }

    private Stream<WorkAssignment> scheduleNonEmpty(List<Painter> painters, Duration totalTime) {
        return Painter.stream(painters)
                .map(painter -> painter.assign(painter.estimateSqMeters(totalTime)));
    }

    private Duration getTotalTime(List<Painter> painters, double sqMeters, Duration upper) {
        return getTotalTime(painters, sqMeters, upper, Duration.ZERO);
    }

    private Duration getTotalTime(List<Painter> painters, double sqMeters, Duration upper, Duration lower) {
        return upper.minus(lower).compareTo(Duration.ofMillis(1)) <= 0
                ? lower
                : getTotalTime(painters, sqMeters, upper, upper.plus(lower).dividedBy(2), lower);
    }

    private Duration getTotalTime(List<Painter> painters, double sqMeters, Duration upper, Duration middle, Duration lower) {
        return this.getTotalSqMeters(painters, middle) > sqMeters
                ? this.getTotalTime(painters, sqMeters, middle, lower)
                : this.getTotalTime(painters, sqMeters, upper, middle);
    }

    private double getTotalSqMeters(List<Painter> painters, Duration time) {
        return Painter.stream(painters)
                .mapToDouble(painter -> painter.estimateSqMeters(time))
                .sum();
    }
}
