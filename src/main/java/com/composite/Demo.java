package com.composite;

import com.composite.common.TimeUtils;
import com.domain.logic.with.streams.Money;
import com.domain.logic.with.streams.Painter;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Demo {
    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.run();
    }

    private MoneyRate perHour(double amount) {
        return MoneyRate.hourly(new Money(new BigDecimal(amount)));
    }

    private List<Painter> createPainters1() {
        return Arrays.asList(
                new ProportionalPainter("Joe", 2.12, this.perHour(44)),
                new ProportionalPainter("Jill", 4.16, this.perHour(60)),
                new ProportionalPainter("Jack", 1.19, this.perHour(16))
        );
    }

    private List<Painter> createPainters2() {
        return Arrays.asList(
                new ProportionalPainter("Jane", 2.27, this.perHour(38)),
                new ProportionalPainter("Jerry", 3.96, this.perHour(57)),
                new CompressionPainter("Jeff", Duration.ofMinutes(12), 19, Duration.ofMinutes(27), 9, this.perHour(70))
        );
    }

    private void print(List<Painter> painters) {
        System.out.println("Painters:");
        for (Painter painter: painters) {
            System.out.println(painter);
        }
    }

    private void print(Painter painter, double sqMeters) {
        Money compensation = painter.estimateCompensation(sqMeters);
        Duration totalTime = painter.estimateTimeToPaint(sqMeters);
        String formattedTime = TimeUtils.format(totalTime);

        System.out.printf(
                "Letting %s paint %.2f sq. meters during %s at total cost %s\n",
                painter.getName(), sqMeters, formattedTime, compensation);
    }

    private static Optional<Painter> findCheapest1(double sqMeters, List<Painter> painters) {
        return painters.stream()
//                .filter(Painter::isAvailable)
                .min(Comparator.comparing(painter -> painter.estimateCompensation(sqMeters)));
    }

    private static Optional<Painter> findCheapest2(double sqMeters, List<Painter> painters) {
        return Painter.stream(painters).available().cheapest(sqMeters);
    }

    private static Money getTotalCost(double sqMeters, List<Painter> painters) {
        return painters.stream()
//                .filter(Painter::isAvailable)
                .map(painter -> painter.estimateCompensation(sqMeters))
                .reduce(Money::add)
                .orElse(Money.ZERO);
    }

    public void workTogether1(double sqMeters, List<Painter> painters) {
        Velocity groupVelocity =
                Painter.stream(painters).available()
                        .map(painter -> painter.estimateVelocity(sqMeters))
                        .reduce(Velocity::add)
                        .orElse(Velocity.ZERO);

        Painter.stream(painters).available()
                .forEach(painter -> {
                    double partialSqMeters = sqMeters * painter.estimateVelocity(sqMeters).divideBy(groupVelocity);
                    this.print(painter, partialSqMeters);
                });
    }

    public void run() {
        List<Painter> painters1 = this.createPainters1();
        double sqMeters = 200;

        this.print(painters1);

        System.out.println();
        System.out.println("Demo #1 - Letting all painters work together");
        this.workTogether1(sqMeters, painters1);

        System.out.println();
        System.out.println("Demo #2 - Letting a composite  painter work");
        Optional<CompositePainter> group1 = CompositePainter.of(painters1);
        group1.ifPresent(group -> this.print(group, sqMeters));

        List<Painter> painters2 = this.createPainters2();
        System.out.println();
        System.out.println("Demo #3 - Compressor and roller painters working together");
        this.workTogether1(sqMeters, painters2);

        Optional<CompositePainter> group2 = CompositePainter.of(painters2);
        group2.ifPresent(group -> this.print(group, sqMeters));

        Optional<CompositePainter> group3 = group2.map(group ->
                Arrays.asList(
                    painters1.get(0), painters1.get(1),
                    new CompressionPainter("Jeff", Duration.ofMinutes(12), 19, Duration.ofMinutes(27), 9, this.perHour(70)),
                    group))
                .flatMap(CompositePainter::of);
        group3.ifPresent(group -> this.print(group, sqMeters));
    }

}