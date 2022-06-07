package com.domain.logic.with.streams;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

public class Money implements Comparable<Money> {
    private BigDecimal amount;

    private BigDecimal getAmount() { return this.amount; }

    public static Money ZERO =
            new Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));

    public Money(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public Money scale(long multiply, long divide) {
        return this.scale(new BigDecimal(multiply), new BigDecimal(divide));
    }

    public Money scale(double factor) {
        BigDecimal newAmount = this.getAmount().multiply(new BigDecimal(factor));
        return new Money(newAmount);
    }

    public Money add(Money other) {
        return new Money(this.getAmount().add(other.getAmount()));
    }

    private Money scale(BigDecimal multiply, BigDecimal divide) {
        return new Money(this.getAmount().multiply(multiply).divide(divide, 2, RoundingMode.HALF_UP));
    }

    public static MoneyStream stream(Stream<Money> moneys) {
        return new MoneyStream(moneys);
    }

    public int compareTo(Money other) {
        return this.getAmount().compareTo(other.amount);
    }

    public String toString() {
        return "$" + this.getAmount();
    }

}
