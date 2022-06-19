package com.refactoring;

import com.domain.logic.with.streams.ForwardingStream;

import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * This custom stream exposes our custom specific behavior
 */
public class DigitStream implements ForwardingStream<Integer> {
    private final Stream<Integer> stream;

    public DigitStream(Stream<Integer> stream) {
        this.stream = stream;
    }

    @Override
    public Stream<Integer> getStream() {
        return this.stream;
    }

    public static DigitStream of(Stream<Integer> stream) {
        return new DigitStream(stream);
    }

    /**
     * Multiply a stream of digits with repeated factors
     * Takes scaled factors and produces a sum of scaled digits
     * This method needs to expose additional methods instead of exposing an int
     * That is why we create the following methods:
     * public StraightNumber multiplyWith(int... factors) {}
     * @param factors
     * @return
     */
//    public int multiplyWith(int... factors) {
    private int multiplyWithAsInt(int... factors) {
        // zip function from google guava:
        // this function - Traverses tow streams in lock step and apply a function to each pair of objects it finds
        // in our case, the function will be to multiply integers
        // zip operation terminates when one of the streams is exhausted
        return Streams.
                .zip(
                        getStream(),
                        this.repeatEndlessly(factors),
                        (a, b) -> a * b
                )
                .mapToInt(n -> n)
                .sum();
        return this.getStream().zip(this.repeatEndlessly(factors)); // zip repeat factors together with the digit of document number
    }

    public StraightNumber multiplyWith(int... factors) {
        return new StraightNumber(this.multiplyWithAsInt(factors));
    }

    private Stream<Integer> repeatEndlessly(int... factors) {
        return Stream
                // push the stream to the output many times, without modifications
                // gives an endless stream of integer arrays
                .iterate(factors, UnaryOperator.identity())
                .flatMapToInt(Arrays::stream) // turn every array into a stream itself
                .boxed(); //
    }
}
