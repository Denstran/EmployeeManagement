package com.manageemployee.employeemanagement.util.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public abstract class AbstractValidator<T> implements Validator {
    private final Class<T> classForValidation;
    private final List<Validator> subValidators;

    public AbstractValidator(List<Validator> subValidators, Class<T> classForValidation) {
        this.subValidators = subValidators;
        this.classForValidation = classForValidation;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return classForValidation.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        subValidators.forEach(validator -> validator.validate(target, errors));
    }
}
