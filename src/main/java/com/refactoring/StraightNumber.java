package com.refactoring;

import java.util.stream.Stream;

/**
 * Domain level representation of the number
 * It will hide the low level programming language level representation of a number
 * Object are the only thor that can help us containing changing requirements
 */
public class StraightNumber {
    private int value;

    public StraightNumber(int value) {
        if(value <= 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    /**
     * Decompose itself into digits
     * We will start by returning a Stream<Integer> - we can see later, if this deserves a class of its own
     * Pure infrastructural code - entirely written in terms of the java programming language
     * All transform specific to java are contained on this method, in this class
     * However this method is returning a generic stream, which is no good
     * Because those streams cannot expose the domain specific methods which I need
     * Instead, we will create and return own own Stream - DigitStream
     * This code remains low level and highly technical
     */
//    public Stream<Integer> getDigitsFromLeastSignificant() {
    public DigitStream getDigitsFromLeastSignificant() {
        // Separating digits
//        return Stream
        return DigitStream.of(Stream
                /**
                 * What do to in every iteration
                 * Start from this.value, and apply n -> n / 10 on every iteration
                 */
                .iterate(this.value, n -> n / 10)
                .takeWhile(n -> n > 0) // take the number while the condition is met
                .map(n -> n % 10) // finite sequence of the least significant digit
        );
    }

    /**
     * Domain specific method exposing the modulo operation
     * @param divisor
     * @return
     */
    public StraightNumber modulo(int divisor) {
        return new StraightNumber(this.value % divisor);
    }

    public int asDigitOr(int substitute) {
        return this.value < 10 ? this.value : substitute;
    }
}
