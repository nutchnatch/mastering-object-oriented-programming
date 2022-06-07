package com.domain.logic.with.streams;

import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;

public class DurationStream implements ForwardingStream<Duration> {
    private Stream<Duration> stream;

    public DurationStream(Stream<Duration> stream) {
        this.stream = stream;
    }

    @Override
    public Stream<Duration> getStream() {
        return this.stream;
    }

    public Duration maxOfMany() {
        return this.stream.max(Duration::compareTo).get();
    }

    public Optional<Duration> min() {
        return this.stream.min(Duration::compareTo);
    }
}
