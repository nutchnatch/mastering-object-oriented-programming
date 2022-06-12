package com.multiway.branching;

import java.time.LocalDate;
import java.util.Optional;

/**
 * This class cannot be implemented as a null object because it has a very starting date
 * It will always be different based on date
 * Doesn't depend on domain-related details. Only depends on core abstractions (Warranty)
 * We can expose them through the abstraction (Warranty) itself.
 * The interface itself and some special cases for NULL pattern can be packaged together
 */
public class LifeTimeWarranty implements Warranty{

    private LocalDate issuedOn;

    public LifeTimeWarranty(LocalDate issuedOn) {
        this.issuedOn = issuedOn;
    }

//    @Override
//    public boolean isValidOn(LocalDate date) {
//        return this.issuedOn.compareTo(date) <= 0;
//    }

    @Override
    public Warranty on(LocalDate date) {
        return date.compareTo(this.issuedOn) < 0 ? Warranty.VOID : this;
    }

    @Override
    public Optional<Warranty> filter(LocalDate date) {
        return date.compareTo(this.issuedOn) >= 0 ? Optional.of(this) : Optional.empty();
    }
}
