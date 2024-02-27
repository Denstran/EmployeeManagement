package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MoneyService {
    public Money sum(Money first, Money second) {
        if (!first.getCurrency().equals(second.getCurrency())) 
            throw new IllegalArgumentException("Нельзя складывать суммы разных валют!");

        BigDecimal sum = first.getAmount().add(second.getAmount());
        return new Money(sum, first.getCurrency());
    }
    
    public Money subtract(Money toSubtractFrom, Money moneyForSubtraction) {
        if (!toSubtractFrom.getCurrency().equals(moneyForSubtraction.getCurrency()))
            throw new IllegalArgumentException("Нельзя складывать суммы разных валют!");

        BigDecimal difference = toSubtractFrom.getAmount().subtract(moneyForSubtraction.getAmount());
        return new Money(difference, toSubtractFrom.getCurrency());
    }

    public boolean isPositive(Money money) {
        BigDecimal amount = money.getAmount();
        return amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public int compareAmounts(Money first, Money second) {
        return first.getAmount().compareTo(second.getAmount());
    }
}
