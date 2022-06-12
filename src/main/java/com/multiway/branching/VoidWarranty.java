package com.multiway.branching;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Null object Pattern
 * Its objects are doing nothing
 * But object implements the expected behavior - never be null
 * It will say that ( when supplied a Null) it is not valid based on whatever dates we ask
 * Methods returning void would be empty
 * Non-Void methods returns constants (false, zero,empty string ...)
 */
public class VoidWarranty implements Warranty {

//    @Override
//    public boolean isValidOn(LocalDate date) {
//        return false;
//    }

    /**
     * Do nothing for this operation
     * @param action
     */
    @Override
    public void claim(Runnable action) {

    }

    @Override
    public Warranty on(LocalDate date) {
        return this;
    }

    @Override
    public Optional<Warranty> filter(LocalDate date) {
        return Optional.empty();
    }
}
