package com.multiway.branching;

import com.multiway.branching.builders.ClaimingRulesBuilder;
import com.multiway.branching.builders.ExhaustiveRulesBuilder;
import com.multiway.branching.builders.PartitioningRulesBuilder;
import com.multiway.branching.states.DeviceStatus;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

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
     * Rules know when to activate any action
     * Constructing the rule should be someone else responsibility - ClaimingRulesBuilder interface
     * ClaimingRulesBuilder - builders are known to be stateful
     * So, we should not accept a ready object, because we cannot guarantee that it is in a clean state
     * When we need to control lifetime of an object, better ask for a factory instead: Supplier<ClaimingRulesBuilder>
     * To implement the Separation of Responsibility, we have the following three points:
     * - This class knows the Article and the status
     * - The Actions should take responsibility about what to do with the data (Article and status)
     * - Rules know when to activate any actions
     * @param article
     * @param status
     */
//    public void claimWarranty(Article article, DeviceStatus status, Optional<LocalDate> sensorFailureDate) {
    public void claimWarranty(
            Supplier<ClaimingRulesBuilder> rulesBuilderFactory,
            Article article,
            DeviceStatus status
    ) {

        LocalDate today = LocalDate.now();

        /**
         * To claim any warranty applicable to the article at hand, on today´s date, given the state of the article
         * This chain of operation, produces a dynamic graph of objects which replace the entire hardcoded structure (if else)
         */
        rulesBuilderFactory.get()
                .onMoneyBack(s -> this.claimMoneyBack(article, today))
                .onClaimExpress(s -> this.claimExpress(article, today))
                .onClaimExtend(s -> this.claimExtended(article, today, s.getFailureDetectedDate()))
                .build()
                .applicableTo(status)
                .ifPresent(Action::apply);

        // Object Oriented code should not depend on reference equality
        // Use equals method instead, whenever there is a semantic definition of equality between objects of some class
//        if (status == DeviceStatus.ALL_FINE) {

        // We can wrap the whole body into a runnable object to hide its arguments
//        Runnable allFineAction = () -> claimMoneyBack(article, today);

//        StatusEqualityRule
//                .match( // --> Rule configuration
//                    OperationalStatus.allFine(),
//                    () -> claimMoneyBack(article, today))
//                .orElse(StatusEqualityRule.match( // --> Rule configuration
//                        OperationalStatus.sensorFailed(),
//                        () -> {
//                            claimMoneyBack(article, today);
//                            claimExpress(article, today);
//                        }))
//                .orElse(StatusEqualityRule.match(
//                        OperationalStatus.visiblyDamaged(),
//                        () -> {}))
//                .applicableTo(status)  // --> Filtering
//                .ifPresent(Action::apply);  // --> Execution


//        Runnable notOperational = () -> {
//            claimMoneyBack(article, today);
//            claimExpress(article, today);
//        };
//        Runnable sensorFailed = () -> {
//            claimMoneyBack(article, today);
//            claimExtended(article, today, sensorFailureDate);
//        };
//        Runnable notOperationalDamage = () -> claimExpress(article, today);
//        Runnable notOperationalSensorFailed = () -> {
//            claimMoneyBack(article, today);
//            claimExpress(article, today);
//            claimExtended(article, today, sensorFailureDate);
//        };
//        Runnable visiblyDamaged = () -> claimExtended(article, today, sensorFailureDate);

//        if (status.equals(OperationalStatus.allFine())) {
//            allFineAction.run();
//        } else if (status.equals(OperationalStatus.notOperational())) {
//
//        } else if (status.equals(OperationalStatus.sensorFailed())) {
//
//        } else if (status.equals(OperationalStatus.notOperational().add(OperationalStatus.visiblyDamaged()))) {
//
//        } else if (status.equals(OperationalStatus.notOperational().add(OperationalStatus.sensorFailed()))) {
//
//        } else if (status.equals(OperationalStatus.visiblyDamaged())) {
//            claimExtended(article, today, sensorFailureDate);
//        } else {
//            claimExpress(article, today);
//            claimExtended(article, today, sensorFailureDate);
//        }

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

    /**
     * The following methods know what to do with the data
     * @param article
     * @param today
     * @param sensorFailureDate
     */
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
//        this.claimWarranty(item, OperationalStatus.allFine(), Optional.empty());
//        this.claimWarranty(item, OperationalStatus.visiblyDamaged(), Optional.empty());
//        this.claimWarranty(item, OperationalStatus.notOperational(), Optional.empty());
//        this.claimWarranty(item, OperationalStatus.sensorFailed(), Optional.empty());

        /**
         * The usage of a factory isolates each run from the others (we could have multiple builders)
         * Because passing the same build object to be repeatedly consumed, could have negative consequences
         */
//        Supplier<ClaimingRulesBuilder> builderFactory = () -> new ExhaustiveRulesBuilder();
        Supplier<ClaimingRulesBuilder> builderFactory = () -> new PartitioningRulesBuilder();
        this.claimWarranty(builderFactory, item, DeviceStatus.allFine());
        this.claimWarranty(builderFactory, item, DeviceStatus.visiblyDamaged());
        this.claimWarranty(builderFactory, item, DeviceStatus.notOperational());
        this.claimWarranty(builderFactory, item, DeviceStatus.notOperational().andVisiblyDamaged());


        LocalDate sensorExamined = LocalDate.now().minus(2, ChronoUnit.DAYS);
        this.claimWarranty(builderFactory, item, DeviceStatus.sensorFailed(sensorExamined));
        this.claimWarranty(builderFactory, item, DeviceStatus.notOperational().andSensorFailed(sensorExamined));
    }
}
