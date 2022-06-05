package com.domain.logic.with.streams;

import java.util.Comparator;
import java.util.Optional;
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
                .filter(Optional::isPresent)
                .map(Optional::get)
        );
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
