package com.manageemployee.employeemanagement.util.validators.additionalValidators;

import com.manageemployee.employeemanagement.model.Money;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
public class MoneyFieldsValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Money.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Money money = (Money) target;
        if (money.getCurrency() == null)
            errors.rejectValue("currency", "", "Валюта не может быть пустой!");
        if (money.getAmount() == null) {
            errors.rejectValue("amount", "", "Количество средств не может быть пустым!");
            return;
        }
        if (money.getAmount().compareTo(BigDecimal.ZERO) < 0)
            errors.rejectValue("amount", "Количество средств должно быть больше 0!");
    }
}
