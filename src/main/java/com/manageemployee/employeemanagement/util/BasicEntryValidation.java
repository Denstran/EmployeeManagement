package com.manageemployee.employeemanagement.util;

import org.springframework.validation.Errors;

/**
 * Abstract class with methods for providing methods for new entry validation and updating entry validation
 * @param <T> - type if validating object
 */
public abstract class BasicEntryValidation<T> {

    /**
     * Method for validating new entries
     * @param t - validated object
     * @param errors - object for storing errors of validated object
     */
    protected abstract void validateNewEntry(T t, Errors errors);

    /**
     * Method for validating updating entries
     * @param t - validated object
     * @param errors - object for storing errors of validated object
     */
    protected abstract void validateUpdatingEntry(T t, Errors errors);
}
