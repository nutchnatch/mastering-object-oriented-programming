package com.refactoring;

/**
 * Wraps the straight number and the algorithm
 * We can now implement strategy pattern and provide different types os algorithms encapsulated on this class
 */
public class DocumentNumber {

    private StraightNumber raw;
    private ControlDigitAlgorithm algorithm;

    public DocumentNumber(int raw, ControlDigitAlgorithm algorithm) {
        this.raw = new StraightNumber(raw);
        this.algorithm = algorithm;
    }

    private int getControlDigit() {
        return this.algorithm.getControlDigit(this.raw);
    }

    @Override
    public String toString() {
        return this.raw.toString() + '-' + ' ' + this.getControlDigit();
    }
}
