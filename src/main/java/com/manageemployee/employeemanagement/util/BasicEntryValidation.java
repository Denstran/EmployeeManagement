package com.manageemployee.employeemanagement.util;

import org.springframework.validation.Errors;

public abstract class BasicEntryValidation<T> {
    protected abstract void validateNewEntry(T t, Errors errors);
    protected abstract void validateUpdatingEntry(T t, Errors errors);
}
