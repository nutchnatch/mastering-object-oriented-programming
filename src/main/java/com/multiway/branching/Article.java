package com.multiway.branching;

import java.time.LocalDate;
import java.util.Optional;

public class Article {

    private Warranty moneyBackGuarantee;
    private Warranty expressWarranty;
    private Warranty effectiveExpressWarranty;
    private Optional<Part> sensor;
    private Warranty extendedWarranty;

    public Article(Warranty moneyBackGuarantee, Warranty expressWarranty) {
        this(moneyBackGuarantee, expressWarranty, Warranty.VOID, Optional.empty(), Warranty.VOID);
    }

    public Article(Warranty moneyBackGuarantee, Warranty expressWarranty, Warranty effectiveExpressWarranty, Optional<Part> sensor, Warranty extendedWarranty) {
        this.moneyBackGuarantee = moneyBackGuarantee;
        this.expressWarranty = expressWarranty;
        this.effectiveExpressWarranty = effectiveExpressWarranty;
        this.sensor = sensor;
        this.extendedWarranty = extendedWarranty;
    }

    public Warranty getMoneyBackGuarantee() {
        return moneyBackGuarantee;
    }

    public Warranty getExpressWarranty() {
        return expressWarranty;
    }

    public Warranty getExtendedWarranty() {
//        return this.sensor == null ? Warranty.VOID : this.sensor.apply(this.extendedWarranty);
        return this.sensor.map(thisSensor -> thisSensor.apply(this.expressWarranty)).orElse(Warranty.VOID);
        //This code is relying on branching and is exposing private data, which is unacceptable
//        if(sensor == null) {
//            return Warranty.VOID;
//        }
//        LocalDate detectedOn = this.sensor.getDefectDetectedOn();
//        if(detectedOn == null) {
//            return Warranty.VOID;
//        }
//        return extendedWarranty.on(detectedOn);
    }

    public Article withVisibilityDamage() {
        return new Article(Warranty.VOID, this.expressWarranty, this.effectiveExpressWarranty, this.sensor, this.extendedWarranty);
    }

    public Article notOperational() {
        return new Article(this.moneyBackGuarantee, this.expressWarranty, this.expressWarranty, this.sensor, this.extendedWarranty);
    }

    public Article install(Part sensor, Warranty extendedWarranty) {
        return new Article(this.moneyBackGuarantee, this.expressWarranty, this.effectiveExpressWarranty, Optional.of(sensor), extendedWarranty);
    }

    /**
     * We see that sensor object may be null, and so cause a NullPointerException
     * We could apply the Null Object Pattern, adding a Part.MISSING object.
     * But, it would count as an other sensor, and if we wanted to count the sensor, the counter would not be ok
     * long sensorCount = items.stream().map(item -> item.getSensor()).filter(sensor -> sensor != null).count()
     * Whether we want to know an object is real or dummy, substitute objects design is not good
     * If an object must know the context in which it is used, then Null Object Pattern is not appropriate
     * Sensors are material object, and we must operate on them, and soon or later we want to know if we have a sensor, or not
     * This is not the case for VoidWarranty, because it is not a countable object, we just use is as behavior
     * @param detectedOn
     * @return
     */
    public Article sensorNotOperational(LocalDate detectedOn) {
//        return this.install(this.sensor.defective(detectedOn), this.extendedWarranty);
        return this.sensor
                .map(thisSensor -> thisSensor.defective(detectedOn))
                .map(defective -> this.install(defective, this.extendedWarranty))
                .orElse(this);
    }
}
