package com.refactoring;

import java.util.stream.Stream;

/**
 * Domain level representation of the number
 * It will hide the low level programming language level representation of a number
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
     */
    public Stream<Integer> getDigitsFromLeastSignificant() {
        // Separating digits
        return Stream
                /**
                 * What do to in every iteration
                 * Start from this.value, and apply n -> n / 10 on every iteration
                 */
                .iterate(this.value, n -> n / 10)
                .takeWhile(n -> n > 0) // take the number while the condition is met
                .map(n -> n % 10) // finite sequence of the least significant digit
    }
}
