package com.refactoring;

public interface ControlDigit {
    static ControlDigitAlgorithm accountingAlgorithm() {
        return PonderingModuleAlgorithm.multipleDigitsModule(
                StraightNumber::getDigitsFromLeastSignificant, 11, 0, new int[]{3,1});
    }

    static ControlDigitAlgorithm salesAlgorithm() {
        return PonderingModuleAlgorithm.singleDigitsModule(
                StraightNumber::getDigitsFromLeastSignificant, 7, 7, new int[]{3,1});
    }

    /**
     * This separation gives you possibility to correlate your code with customer expectation
     * @return
     */
    static ControlDigitAlgorithm salesAlgorithmMay2017() {
        return PonderingModuleAlgorithm.singleDigitsModule(
                StraightNumber::getDigitsFromLeastSignificant, 7, 7, new int[]{3,1});
    }
}
