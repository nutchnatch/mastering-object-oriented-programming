package com.multiway.branching;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class Demo {

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.run();
    }

    /**
     * If we add more criterion, more branching must be added
     * If this analysis is required on a different scenarios, this entire structure, must be replicated somewhere
     * The answer is to move responsibility into objects
     * Whenever requirements change, we have to we must modify this method
     * Proliferation of low level representation
     * Hardcoded branches decisions
     * Expected qualities:
     *  - Flexible behavior
     *  - Support for changing rules
     * Each segment code should be turned into an object
     * This objects would be sensitive to a concrete state
     * Each object will represent a rule - a chain of rules
     * A rule condition should either be satisfied, or not
     * If not, it transfer control to the next rule in the line
     * Actions can be turned into active rules
     * Action is a function which doesn't produce a result, instead just mutates the state internally
     * Optional sensorFailureDate parameter is a flaw, because its value is coupled with the value of the DeviceStatus status
     * If we see two variables coupled by a rigid rule, we should immediately identify them as a design flaw
     * Optional sensorFailureDate can be empty (then status should not exist) or non-empty (must include the sensor (failed) field)
     * Methods signature must be absolutely correct, so they can behave correctly on all usages that pass the build
     * Code that pass the build, must be correct
     * Optional<LocalDate> sensorFailureDate should be part of another object
     * Rules support that by design, collecting data while preparing the act
     * DeviceStatus should become a component of a larger object (OperationalStatus)
     * @param article
     * @param status
     */
//    public void claimWarranty(Article article, DeviceStatus status, Optional<LocalDate> sensorFailureDate) {
    public void claimWarranty(Article article, OperationalStatus status) {

        LocalDate today = LocalDate.now();
        // Object Oriented code should not depend on reference equality
        // Use equals method instead, whenever there is a semantic definition of equality between objects of some class
//        if (status == DeviceStatus.ALL_FINE) {

        // We can wrap the whole body into a runnable object to hide its arguments
        Runnable allFineAction = () -> claimMoneyBack(article, today);

        StatusEqualityRule
                .match( // --> Rule configuration
                    DeviceStatus.allFine(),
                    () -> claimMoneyBack(article, today))
                .orElse(StatusEqualityRule.match( // --> Rule configuration
                        DeviceStatus.sensorFailed(),
                        () -> {
                            claimMoneyBack(article, today);
                            claimExpress(article, today);
                        }))
                .orElse(StatusEqualityRule.match(
                        DeviceStatus.visiblyDamaged(),
                        () -> {}))
                .orElse(StatusEqualityRule.match(

                ))
                .applicableTo(status)  // --> Filtering
                .ifPresent(Action::apply);  // --> Execution


        Runnable notOperational = () -> {
            claimMoneyBack(article, today);
            claimExpress(article, today);
        };
        Runnable sensorFailed = () -> {
            claimMoneyBack(article, today);
            claimExtended(article, today, sensorFailureDate);
        };
        Runnable notOperationalDamage = () -> claimExpress(article, today);
        Runnable notOperationalSensorFailed = () -> {
            claimMoneyBack(article, today);
            claimExpress(article, today);
            claimExtended(article, today, sensorFailureDate);
        };
        Runnable visiblyDamaged = () -> claimExtended(article, today, sensorFailureDate);

        if (status.equals(DeviceStatus.allFine())) {
            allFineAction.run();
        } else if (status.equals(DeviceStatus.notOperational())) {

        } else if (status.equals(DeviceStatus.sensorFailed())) {

        } else if (status.equals(DeviceStatus.notOperational().add(DeviceStatus.visiblyDamaged()))) {

        } else if (status.equals(DeviceStatus.notOperational().add(DeviceStatus.sensorFailed()))) {

        } else if (status.equals(DeviceStatus.visiblyDamaged())) {
            claimExtended(article, today, sensorFailureDate);
        } else {
            claimExpress(article, today);
            claimExtended(article, today, sensorFailureDate);
        }

        System.out.println("-----------------");
    }

    /**
     * This client side method is the perfect representation of what will happen when its representation is not encapsulated
     * You will have to do all the manipulation your self
     * All the complexity and all the bugs will be yours
     * All the maintenance hell will be unleashed upon you too
     * Representation should be closed into an object
     * Enumeration shows a lack of Object Oriented design
     * Methods signature must be absolutely correct, so they can behave correctly on all usages that pass the build
     * Code that pass the build, must be correct
     * @param article
     * @param today
     * @param sensorFailureDate
     */
//    private void claimExtended(Article article, LocalDate today, Optional<LocalDate> sensorFailureDate) {
//        // A consumer
//        sensorFailureDate
//                .flatMap(date -> article.getExtendedWarranty().filter(date))
//                .ifPresent(warranty -> warranty.on(today).claim(this::offerSensorRepair));
//    }
    private void claimExtended(Article article, LocalDate today, LocalDate sensorFailureDate) {
        article.getExtendedWarranty().filter(sensorFailureDate)
                .ifPresent(warranty -> warranty.on(today).claim(this::offerSensorRepair));
    }

    private void claimExpress(Article article, LocalDate today) {
        article.getExpressWarranty().on(today).claim(this::offerRepair);
    }

    private void claimMoneyBack(Article article, LocalDate today) {
        article.getMoneyBackGuarantee().on(today).claim(this::offerMoneyBack);
    }

    private void offerMoneyBack() {
        System.out.println("Offer money back");
    }

    private void offerRepair() {
        System.out.println("Offer repair");
    }

    private void offerSensorRepair() {
        System.out.println("Offer sensor replacement");
    }


    public void run() {
        LocalDate sellingDate = LocalDate.now().minus(40, ChronoUnit.DAYS);
        Warranty moneyBack1 = new TimeLimitWarranty(sellingDate, Duration.ofDays(60));
        Warranty warranty1 = new TimeLimitWarranty(sellingDate, Duration.ofDays(365));

        Part sensor = new Part(sellingDate);
        Warranty sensorWarranty = new TimeLimitWarranty(sellingDate, Duration.ofDays(90));

        Article item = new Article(moneyBack1, warranty1).install(sensor, sensorWarranty);

//        this.claimWarranty(item);
//        this.claimWarranty(item.withVisibilityDamage());
//        this.claimWarranty(item.notOperational().withVisibilityDamage());
//        this.claimWarranty(item.notOperational());
//
//        LocalDate sensorExamined = LocalDate.now().minus(2, ChronoUnit.DAYS);
//        this.claimWarranty(item.sensorNotOperational(sensorExamined));
//        this.claimWarranty(item.notOperational().sensorNotOperational(sensorExamined));

        // I am forced to cover the cases which doesn't apply to my current situation
        // Optional.empty should be encapsulated on an object
        // But with this logic, I have no object and so, that variable is under my responsibility
        this.claimWarranty(item, DeviceStatus.allFine(), Optional.empty());
        this.claimWarranty(item, DeviceStatus.visiblyDamaged(), Optional.empty());
        this.claimWarranty(item, DeviceStatus.notOperational(), Optional.empty());
        this.claimWarranty(item, DeviceStatus.sensorFailed(), Optional.empty());

        LocalDate sensorExamined = LocalDate.now().minus(2, ChronoUnit.DAYS);
        this.claimWarranty(item, DeviceStatus.sensorFailed(), Optional.of(sensorExamined));
        this.claimWarranty(item, DeviceStatus.notOperational().add(DeviceStatus.sensorFailed()), Optional.of(sensorExamined));
    }
}
