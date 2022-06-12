package com.multiway.branching;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class DemoOld {

    public static void main(String[] args) {
        DemoOld demo = new DemoOld();
        demo.run();
    }

    /**
     * Major problem with procedural coding style appears when we try to maintain the code
     * The switch approach is entirely made on the client side,
     * Which means that the consumer knows how to behave on each of these situations
     * Change rules a bit, and you will find yourself changing the code which is consuming the feature
     * Because of that you will be always testing and deploying again
     * That is not how programming should behave
     * in this method we can find two issues:
     * #1 - Branching around flag
     * #2 - Passing state via  multiple variables
     * Those variables should be part of a single object
     * In OOP, Domain logic should be encapsulated inside domain models
     * A consumer would declare what it needs and a proper object would be triggered
     * With no objects around, the consumer must know all the domain rules
     * Should any rule added, or changed, all the consumer of these data must be changed
     * Code change is the principal origin of defects and instability
     * The primary problem of this design is that is is constructed around the State Representation with the following parameters:
     * DeviceStatus status, Optional<LocalDate> sensorFailureDate
     * That is where we lost objects
     * The notion that sensorFailureDate applies only to some cases but not to others, has been lost
     * Representation should always encapsulated into an object
     * Only behavior should be accessed by object reference
     * @param article
     * @param status
     */
    public void claimWarranty(Article article, DeviceStatusOld status, Optional<LocalDate> sensorFailureDate) {

        LocalDate today = LocalDate.now();
        switch(status) {
            case ALL_FINE:
                claimMoneyBack(article, today);
                break;
            case NOT_OPERATIONAL:
                claimMoneyBack(article, today);
                claimExpress(article, today);
                break;
            case VISIBLY_DAMAGED:
                break;
            case SENSOR_FAILED:
                claimMoneyBack(article, today);
                claimExtended(article, today, sensorFailureDate);
                break;
            case NOT_OPERATIONAL_DAMAGE:
                claimExpress(article, today);
            case NOT_OPERATIONAL_SENSOR_FAILED:
                claimMoneyBack(article, today);
                claimExpress(article, today);
                claimExtended(article, today, sensorFailureDate);
            case DAMAGED_SENSOR_FAILED:
                claimExtended(article, today, sensorFailureDate);
            case NOT_OPERATIONAL_DAMAGE_SENSOR_FAILED:
                claimExpress(article, today);
                claimExtended(article, today, sensorFailureDate);
            default:
                break;
        }

        // The same code above could be written as the following
//        if(status == DeviceStatus.ALL_FINE) {
//            article.getMoneyBackGuarantee().on(today).claim(this::offerMoneyBack);
//        } else if(status == DeviceStatus.NOT_OPERATIONAL) {
//            article.getMoneyBackGuarantee().on(today).claim(this::offerMoneyBack);
//            article.getExpressWarranty().on(today).claim(this::offerRepair);
//        } else if(status == DeviceStatus.VISIBLY_DAMAGE) {
//
//        } else if(status == DeviceStatus.SENSOR_FAILED) {
//            article.getMoneyBackGuarantee().on(today).claim(this::offerMoneyBack);
//            article.getExtendedWarranty().on(today).claim(this::offerSensorRepair);
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
     * @param article
     * @param today
     * @param sensorFailureDate
     */
    private void claimExtended(Article article, LocalDate today, Optional<LocalDate> sensorFailureDate) {
        // A consumer
        sensorFailureDate
                .flatMap(date -> article.getExtendedWarranty().filter(date))
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
        this.claimWarranty(item, DeviceStatusOld.ALL_FINE, Optional.empty());
        this.claimWarranty(item, DeviceStatusOld.VISIBLY_DAMAGED, Optional.empty());
        this.claimWarranty(item, DeviceStatusOld.NOT_OPERATIONAL, Optional.empty());
        this.claimWarranty(item, DeviceStatusOld.SENSOR_FAILED, Optional.empty());

        LocalDate sensorExamined = LocalDate.now().minus(2, ChronoUnit.DAYS);
        this.claimWarranty(item, DeviceStatusOld.SENSOR_FAILED, Optional.of(sensorExamined));
        this.claimWarranty(item, DeviceStatusOld.NOT_OPERATIONAL.add(DeviceStatusOld.SENSOR_FAILED), Optional.of(sensorExamined));
    }
}
