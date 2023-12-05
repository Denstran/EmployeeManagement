package com.manageemployee.employeemanagement.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Money implements Comparable<Money> {

    @Min(value = 1, message = "Количество средств должно быть больше нуля!")
    @NotNull(message = "Количество средств не может быть пустым")
    private BigDecimal amount;

    @NotNull(message = "Валюта не должны быть пустой!")
    private Currency currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return getAmount() + " " + getCurrency();
    }

    public static Money getMoneyFromString(String s) {
        String[] strings = s.split(" ");

        return new Money(new BigDecimal(strings[0]), Currency.getInstance(strings[1]));
    }

    @Override
    public int compareTo(Money o) {
        if (o.getCurrency() == null || !o.getCurrency().equals(this.currency))
            throw new IllegalArgumentException("Операция сравнения денежных единиц различных валют не поддерживается!");

        return this.amount.compareTo(o.getAmount());
    }
}
