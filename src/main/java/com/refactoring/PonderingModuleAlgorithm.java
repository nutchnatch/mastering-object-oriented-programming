package com.refactoring;

import java.util.function.Function;

public class PonderingModuleAlgorithm implements ControlDigitAlgorithm {
    private Function<StraightNumber, DigitStream> digitExtractor;
    private int[] factors;
    private Function<StraightNumber, Integer> reduce;

    /**
     * All data and behaviors are injectable, the algorithm does not know them
     * @param digitExtractor
     * @param factors
     * @param reduce
     */
    public PonderingModuleAlgorithm(
            Function<StraightNumber, DigitStream> digitExtractor,
            int[] factors, Function<StraightNumber,
            Integer> reduce
    ) {
        this.digitExtractor = digitExtractor;
        this.factors = factors;
        this.reduce = reduce;
    }

//    public PonderingModuleAlgorithm(Function<StraightNumber, DigitStream> digitExtractor, int[] factors) {
//        this.digitExtractor = digitExtractor;
//        this.factors = factors;
//    }

//    private PonderingModuleAlgorithm(Function<StraightNumber, DigitStream> digitExtractor) {
//        this.digitExtractor = digitExtractor;
//    }

    /**
     * One of the allowed constructur
     * @param digitExtractor
     * @param divisor
     * @param substitute
     * @param factors
     * @return
     */
    public static ControlDigitAlgorithm multipleDigitsModule(
            Function<StraightNumber, DigitStream> digitExtractor,
            int divisor, int substitute, int[] factors
    ) {
        return new PonderingModuleAlgorithm(
                digitExtractor, factors, n -> n.modulo(divisor).asDigitOr(substitute));
    }

    public static ControlDigitAlgorithm singleDigitsModule(
            Function<StraightNumber, DigitStream> digitExtractor,
            int divisor, int substitute, int[] factors
    ) {
        return new PonderingModuleAlgorithm(
                digitExtractor, factors, n -> n.modulo(divisor).asDigit(substitute));
    }

    @Override
    public int getControlDigit(StraightNumber number) {
        /**
         * Base the getDigit om a strategy provided from outside
         */
//       return getDigits(number) // This is what this object shall do for us - behavior
//        return this.digitExtractor.apply(number)
//                /**
//                 * This is what we are doing with each number
//                 * We passes the factors and leave the rest with infrastructural code
//                 */
////                .multiplyWith(3, 1)// factors can be customizable, so we can add them to constructor
//                .multiplyWith(factors)
//                .modulo(11) // modulo can be customized too. So we can inject the function which will reduce straight numbers into a digit
//                .asDigitOr(0);
        return this.reduce.apply(
                this.digitExtractor
                        .apply(number)
                        .multiplyWith(factors));
    }

    /**
     * This is another change on the requirements
     * Client wants to change the way we read digits
     * Since it is an isolated operation, it is easier to change it
     * Anything existing in isolation, can be turned into ab object and then substitute it
     * We can turn it into a strategy, and apply that strategy from the outside
     */
//    private DigitStream getDigits(StraightNumber number) {
//        return number.getDigitsFromLeastSignificant();
//    }
}
