package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.util.validationgroups.DefaultGroup;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
@Setter
public class Money {
    @PositiveOrZero(message = "Количество средств должно быть положительным!", groups = {DefaultGroup.class})
    private Double amount;
    public Money(Money money) {
        this.amount = money.getAmount();
    }

    public static Money sum(Money first, Money second) {
        return new Money(first.getAmount() + second.getAmount());
    }
    public static Money subtract(Money toSubtractFrom, Money moneyForSubtraction) {
        return new Money(toSubtractFrom.getAmount() - moneyForSubtraction.getAmount());
    }

    public static boolean isPositive(Money money) {
        return money.amount > 0;
    }

    public static Money abs(Money money) {
        double absoluteAmount = Math.abs(money.getAmount());
        return new Money(absoluteAmount);
    }

    public static int compareTo(Money first, Money second) {
        return Double.compare(first.getAmount(), second.getAmount());
    }

    @Override
    public String toString() {
        return String.valueOf(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
