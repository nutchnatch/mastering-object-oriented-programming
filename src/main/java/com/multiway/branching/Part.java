package com.multiway.branching;

import java.time.LocalDate;
import java.util.Optional;

public class Part {

    private LocalDate installmentDate; // date of part installation
//    private LocalDate defectDetectedOn; // date when a defect was diagnosed
    private Optional<LocalDate> defectDetectedOn; // date when a defect was diagnosed

    public Part(LocalDate installmentDate) {
//        this(installmentDate, null);
        this(installmentDate, Optional.empty());
    }

    public Part(LocalDate installmentDate, Optional<LocalDate> defectDetectedOn) {
        this.installmentDate = installmentDate;
        this.defectDetectedOn = defectDetectedOn;
    }

    public Part defective(LocalDate detectedOn) {
        return new Part(this.installmentDate, Optional.of(detectedOn));
    }

    /**
     * Optional object tells what to do when there are data, and what not to do when there are no data
     * @param partWarranty
     * @return
     */
    public Warranty apply(Warranty partWarranty) {
//        return this.defectDetectedOn == null? Warranty.VOID : Warranty.lifetime(this.defectDetectedOn);
        return this.defectDetectedOn
                .flatMap(date -> partWarranty.filter(date).map(warranty -> Warranty.lifetime(date)))
                .orElse(Warranty.VOID);
    }
}
