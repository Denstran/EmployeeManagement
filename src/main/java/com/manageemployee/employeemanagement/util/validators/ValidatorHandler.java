package com.manageemployee.employeemanagement.util.validators;

import org.springframework.validation.Errors;

import java.util.List;

public abstract class ValidatorHandler<T, V extends Validator<T>> implements Validator<T> {
    private final List<V> validators;

    public ValidatorHandler(List<V> validators) {
        this.validators = validators;
    }

    @Override
    public void validate(T target, Errors errors) {
        validators.forEach(v -> v.validate(target, errors));
    }
}
