package com.domain.logic.with.streams;

import java.util.stream.Stream;

public class MoneyStream implements ForwardingStream<Money> {
    private Stream<Money> stream;

    public MoneyStream(Stream<Money> stream) {
        this.stream = stream;
    }

    @Override
    public Stream<Money> getStream() {
        return this.stream;
    }

    public Money sum() {
        return this.stream.reduce(Money::add).orElse(Money.ZERO);
    }
}
