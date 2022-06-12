package com.multiway.branching;

import java.time.LocalDate;
import java.util.Optional;

public interface Warranty {

    /**
     * Boolean method is a design flaw
     * Don't tell caller WHEN to do something
     * Classes cannot be used as helper
     * Operations shall be in object they concern. Expose the proper operation instead
     * In this case, we can tell Warranty to go claim it self
     */
//    boolean isValidOn(LocalDate date);

    /**
     * This is the object oriented way, instead of using isValidOn
     * Receives a callback to execute, which turns the claim consumption unconditional
     */
    default void claim(Runnable action) {
        action.run();
    }

    /**
     * Filtering API style
     * Filters Warranty by date
     * Returns the version of itself on a given date
     * Removes the need for Boolean methods, saying when it is applicable or not
     * On the result of this filter, we can call claim unconditionally
     * @param date
     * @return
     */
    Warranty on(LocalDate date);

    Optional<Warranty> filter(LocalDate date);

    Warranty VOID = new VoidWarranty();

    static Warranty lifetime(LocalDate issuedOn) {
        return new LifeTimeWarranty(issuedOn);
    }
}
