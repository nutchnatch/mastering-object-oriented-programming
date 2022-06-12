package com.multiway.branching;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

public class TimeLimitWarranty implements Warranty{
    private LocalDate dateIssued;
    private Duration validFor;

    public TimeLimitWarranty(LocalDate dateIssued, Duration validFor) {
        this.dateIssued = dateIssued;
        this.validFor = validFor;
    }

//    @Override
//    public boolean isValidOn(LocalDate date) {
//        return this.dateIssued.compareTo(date) <= 0 &&
//                this.getExpiredDate().compareTo(date) > 0;
//    }

    /**
     * This method branches around the argument that cannot be predicted
     * This branching cannot be replaced with any unconditional operation
     * Branching is done inside de feature producer, and no consumer will ever see it
     * And consumers will use it unconditionally
     * @param date
     * @return
     */
    @Override
    public Warranty on(LocalDate date) {
        return date.compareTo(this.dateIssued) < 0 ? Warranty.VOID
                :date.compareTo(this.getExpiredDate()) > 0 ? Warranty.VOID
                :this;

    }

    @Override
    public Optional<Warranty> filter(LocalDate date) {
        return date.compareTo(this.dateIssued) >= 0 ? Optional.of(this) : Optional.empty();
    }

    public LocalDate getExpiredDate() {
        return dateIssued.plusDays(this.getValidForDays());
    }

    public long getValidForDays() {
        return validFor.toDays();
    }
}
