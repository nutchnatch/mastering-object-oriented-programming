package com.domain.logic.with.streams;

import com.composite.CompositePainter;
import com.composite.PaintingScheduler;
import com.composite.WorkAssignment;

import java.time.Duration;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Concrete domain related functions
 */
public class PaintersStream implements ForwardingStream<Painter> {
    private final Stream<Painter> stream;

    public PaintersStream(Stream<Painter> stream) {
        this.stream = stream;
    }

    @Override
    public Stream<Painter> getStream() {
        return this.stream;
    }

    /**
     * General purpose filter method is now encapsulated, and callers don't have to do it them self
     * @return
     */
    public PaintersStream available() {
        return new PaintersStream(this.getStream()
                .map(Painter::available)
                .map(OptionalPainter::asOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
        );
    }

    public double estimateSqMeters(Duration time) {
        return this.getStream().mapToDouble(painter -> painter.estimateSqMeters(time))
                .sum();
    }

    public OptionalPainter workTogether(PaintingScheduler scheduler) {
        return CompositePainter.of(this.stream.collect(Collectors.toList()), scheduler);
    }

    public WorkStream assign(Duration time) {
        return WorkAssignment.stream(this.getStream()
                .map(painter -> painter.assign(painter.estimateSqMeters(time))));
    }

    public PaintersStream with(Painter other) {
        return new PaintersStream(Stream.concat(this.stream, Stream.of(other)));
    }

    public PaintersStream with(Optional<Painter> other) {
        return other.map(this::with).orElse(this);
    }

    public PaintersStream with(OptionalPainter other) {
        return this.with(other.asOptional());
    }

    public DurationStream tomesToPaint(double sqMeters) {
        return new DurationStream(this.getStream().map(painter -> painter.estimateTimeToPaint(sqMeters)));
    }

    /**
     * This logic is encapsulated and not exposed to external
     * @param sqMeters
     * @return
     */
    public Optional<Painter> cheapest(double sqMeters) {
        return this.getStream().min(Comparator.comparing(painter -> painter.estimateCompensation(sqMeters)));
    }
}
