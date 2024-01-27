package com.manageemployee.employeemanagement.util.validators;

import org.springframework.validation.Errors;

public interface Validator<T> {
    void validate(T target, Errors errors);
}
