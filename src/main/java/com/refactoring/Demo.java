package com.refactoring;

/**
 * Code quality can be determined by the following:
 * - Code is short?
 * - Conde is maintainable?
 * - Code is flexible?
 * - Code is "nice"?
 *
 * Complex requirements lead to longer implementation (imagine calculating social security number)
 * This kind of code bellow does not produce a short code
 * Here are the requirements of this algorithm:
 * - Multiply every other digit by three, starting by the first digit on the right side (lsd)
 * - Sum the result up
 * - Take modulo eleven of the sum
 * - Or substitute by zero
 *
 * Some problems may com from translating the requirements into an algorithm:
 * - Procedural code doesn't match the requirements in a spoken language (does not read as when we read the requirements)
 * - What happens if the requirements change? How do we extend this implementation to support extended requirements?
 * From this, the truth is:
 *  - Requirements changes around an access which is defined by a logic of a man
 *  - Implementation changes around an access which is defined by a grammar of a programming language
 *  Maintainability/Flexibility - Requirements changes:
 *  - Customer now says that digits must be read from left to right
 *  -> This change in the requirement would need a big "hammer" to fix the code and embracing this change
 *  Problems with the code:
 *  - It is mixing infrastructural code with domain related code
 *      - the following parts are infrastructural:
 *      while(number > 0) {
 *      sum += 3 * digit;
 *      sum += digit;
 *      number /= 10;
 *      - none of those are substantial for the algorithm
 * The first step to refactor this code is to give names to domain related concepts
 *
 *
 *
 */
public class Demo {

    public static void main(String[] args) {
        Demo demo = new Demo();
        int number = 123456;
        int res = demo.getControlDigit(number);
        System.out.println(number + "-" +res);
    }

    private int getControlDigit(int number) {
        int sum = 0;
        boolean isOddPos = true;

        while(number > 0) {
            /**
             * Extracts the las significant digit (lsd)
             * if number = 123456 -> digit = 6
             */
            int digit = number % 10;
            if(isOddPos) {
                sum += 3 * digit;
            } else {
                sum += digit;
            }

            /**
             * Extracts the rest of the number, removing tha lsd
             * if number = 123456 -> number /= 10 => 12345
             */
            number /= 10;
            isOddPos = !isOddPos;
        }

        int modulo = sum % 11;
        /**
         * If modulo has more than one digit
         */
        if(modulo > 9) {
            modulo = 0;
        }
        return modulo;
    }

    /**
     * Giving names to domain related concepts
     * @param number
     * @return
     */
    private int getControlDigit(StraightNumber number) { //That is how human see the numbers - straight line of digits
        number.getDigitsFromLeastSignificant() // This is what this object shall do for us - behavior
                /**
                 * This is what we are doing with each number
                 * We passes the factors and leave the rest with infrastructural code
                 */
                .multiplyWith(3, 1)
                .module(11)
                .asDigitOr(0);
    }
}
