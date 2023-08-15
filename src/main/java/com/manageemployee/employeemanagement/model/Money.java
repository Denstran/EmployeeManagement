package com.manageemployee.employeemanagement.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Money {

    @Min(value = 1, message = "Количество денег должно быть больше нуля!")
    @NotNull
    private BigDecimal amount;

    @NotBlank(message = "Валюта не должна быть пустой!")
    @NotNull
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
}
